package com.totvs.infrastructure.security;


import com.totvs.domain.account.model.Authority;
import com.totvs.infrastructure.exception.UserNotActivatedException;
import com.totvs.domain.account.repository.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) {
        log.debug("Authenticating {}", email);

        if (new EmailValidator().isValid(email, null)) {
            return userRepository
                .findOneWithAuthoritiesByEmail(email)
                .map(user -> createSpringSecurityUser(email, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found in the database"));
        }

        String lowerEmail = email.toLowerCase(Locale.ENGLISH);
        return userRepository
            .findOneWithAuthoritiesByEmail(lowerEmail)
            .map(user -> createSpringSecurityUser(lowerEmail, user))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowerEmail + " was not found in the database"));
    }

    private User createSpringSecurityUser(String lowercaseLogin,
                                          com.totvs.domain.account.model.User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user
            .getAuthorities()
            .stream()
            .map(Authority::getPermissionName)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
