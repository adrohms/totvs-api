package com.totvs.application.account.resource;

import com.totvs.domain.account.model.User;
import com.totvs.application.account.constants.AccountConstants;
import com.totvs.application.account.dto.AuthenticationResponseRec;
import com.totvs.application.account.dto.LoginRec;
import com.totvs.application.account.dto.UserRec;
import com.totvs.application.account.enumeration.AuthenticationStatus;
import com.totvs.application.common.exception.EmailAlreadyUsedException;
import com.totvs.application.account.exception.InvalidPasswordException;
import com.totvs.application.account.exception.LoginAlreadyUsedException;
import com.totvs.application.account.mapper.UserMapper;
import com.totvs.domain.account.repository.UserRepository;
import com.totvs.domain.account.service.AccountService;
import com.totvs.infrastructure.security.jwt.TokenProvider;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/public/auth")
public class AuthPublicResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthPublicResource(
            UserRepository userRepository,
            AccountService accountService,
            TokenProvider tokenProvider,
            AuthenticationManagerBuilder authenticationManagerBuilder,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param userRec the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    public ResponseEntity registerAccount(@Valid @RequestBody UserRec userRec) {
        if (isPasswordLengthInvalid(userRec.password())) {
            throw new InvalidPasswordException();
        }
        User user = this.accountService.registerUser(userRec, userRec.password());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseRec> authorize(@Valid @RequestBody LoginRec loginRec) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRec.email(),
                loginRec.password()
        );

        Authentication authentication = this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.tokenProvider.createToken(authentication, loginRec.rememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();

        HttpCookie httpCookie = ResponseCookie
                .from("accessToken", jwt)
                .maxAge(7200)
                .httpOnly(true)
                .path("/")
                .build();

        httpHeaders.add(HttpHeaders.SET_COOKIE, httpCookie.toString());
        AuthenticationResponseRec authenticationBody = new AuthenticationResponseRec(
                AuthenticationStatus.AUTHENTICATED,
                "Usuário autenticado com sucesso!"
        );

        return new ResponseEntity<>(authenticationBody, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthenticationResponseRec> logout() {

        SecurityContextHolder.clearContext();
        HttpHeaders httpHeaders = new HttpHeaders();

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = currentTime.plusSeconds(300);

        httpHeaders.add(HttpHeaders.SET_COOKIE,
                ResponseCookie
                        .from("accessToken", "")
                        .maxAge(0)
                        .httpOnly(true)
                        .path("/")
                        .build()
                        .toString());

        AuthenticationResponseRec authenticationBody = new AuthenticationResponseRec(
                AuthenticationStatus.UNAUTHENTICATED,
                "A sessão do usuário foi encerrada com sucesso!"
        );

        return new ResponseEntity<>(authenticationBody, httpHeaders, HttpStatus.OK);
    }




    private static boolean isPasswordLengthInvalid(String password) {
        return (
                StringUtils.isEmpty(password) ||
                        password.length() < AccountConstants.PASSWORD_MIN_LENGTH ||
                        password.length() > AccountConstants.PASSWORD_MAX_LENGTH
        );
    }

}