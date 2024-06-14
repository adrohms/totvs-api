package com.totvs.application.account.resource;

import com.totvs.application.account.dto.UserVMRec;
import com.totvs.application.account.mapper.UserMapper;
import com.totvs.domain.account.repository.UserRepository;
import com.totvs.domain.account.service.AccountService;
import com.totvs.infrastructure.security.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
public class AccountResource {

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

    public AccountResource(
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

    @GetMapping("/user")
    public UserVMRec getCurrentUser() {

        return this.accountService
                .getUserWithAuthorities()
                .map(user -> {
                    UserVMRec userVMRec = new UserVMRec(
                            user.getId(),
                            user.getEmail(),
                            user.getAuthorities().stream()
                                    .map(authority -> authority.getPermissionName()).collect(Collectors.toSet()),
                            user.isActivated()
                    );
                    return userVMRec;
                })
                .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

}