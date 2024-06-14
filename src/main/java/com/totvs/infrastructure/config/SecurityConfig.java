package com.totvs.infrastructure.config;

import com.totvs.infrastructure.security.constants.AuthoritiesConstants;
import com.totvs.infrastructure.security.jwt.JWTConfigurer;
import com.totvs.infrastructure.security.jwt.JWTFilter;
import com.totvs.infrastructure.security.jwt.TokenProvider;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Import(SecurityProblemSupport.class)
@Log4j
public class SecurityConfig {

    private final TotvsProperties totvsProperties;
    private final TokenProvider tokenProvider;
    private final SecurityProblemSupport problemSupport;
    private final CorsFilter corsFilter;

    public SecurityConfig(
            TokenProvider tokenProvider,
            TotvsProperties totvsProperties,
            SecurityProblemSupport problemSupport, CorsFilter corsFilter) {
        this.tokenProvider = tokenProvider;
        this.problemSupport = problemSupport;
        this.totvsProperties = totvsProperties;
        this.corsFilter = corsFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter(tokenProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling  -> 
                        exceptionHandling
                                .authenticationEntryPoint(problemSupport)
                                .accessDeniedHandler(problemSupport))
                .headers(headers ->
                        headers
                                .frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin())
                                .contentSecurityPolicy(securityPolicyConfig ->
                                        securityPolicyConfig
                                                .policyDirectives(totvsProperties.getSecurity().getContentSecurityPolicy())
                                )
                                .referrerPolicy(referrerPolicyConfig ->
                                        referrerPolicyConfig
                                                .policy(ReferrerPolicyHeaderWriter
                                                                .ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                                )
                                .permissionsPolicy(permissionsPolicyConfig -> permissionsPolicyConfig
                                                .policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), " +
                                                        "magnetometer=(), microphone=(), midi=(), payment=(), " +
                                                        "sync-xhr=()")
                                )
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(
                                "/api/public/**",
                                "/content/**",
                                "/swagger-ui/**",
                                "/test/**",
                                "/favicon.ico").permitAll()
                        .requestMatchers("/api/admin/**").hasAnyAuthority(AuthoritiesConstants.ADMIN)
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .apply(securityConfigurerAdapter());


        return httpSecurity.build();
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider, corsFilter);
    }

}
