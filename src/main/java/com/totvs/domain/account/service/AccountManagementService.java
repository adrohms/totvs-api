package com.totvs.domain.account.service;

import com.totvs.application.account.dto.UserMngtVM;
import com.totvs.application.common.mapper.PersonMapper;
import com.totvs.domain.account.model.User;
import com.totvs.domain.account.repository.AuthorityRepository;
import com.totvs.domain.account.repository.UserRepository;
import com.totvs.application.account.mapper.UserMapper;
import com.totvs.domain.common.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountManagementService {

    private final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final PersonRepository personRepository;
    private final UserMapper userMapper;
    private final PersonMapper personMapper;

    public AccountManagementService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository,
            PersonRepository personRepository,
            UserMapper userMapper,
            PersonMapper personMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.personRepository = personRepository;
        this.userMapper = userMapper;
        this.personMapper = personMapper;
    }

    public Optional<User> activateUserById(Long id) {
        log.debug("Activating user for activation key {}", id);
        return userRepository
                .findOneById(id)
                .map(user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    public Page<UserMngtVM> getAllUsers(UserMngtVM userFilter, Pageable pageable) {
        UserMngtVM updatedUserFilter = this.setAuthoritiesNullIfItIsEmpty(userFilter);

        Page<User> users = userRepository.findUsersByFilter(updatedUserFilter, pageable);

        Page<UserMngtVM> usersMngt = users.map(user -> new UserMngtVM(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getAuthorities().stream().map(authority -> authority.getPermissionName()).collect(Collectors.toSet()),
                user.isActivated()
        ));

        return usersMngt;
    }

    private UserMngtVM setAuthoritiesNullIfItIsEmpty(UserMngtVM userFilter) {
        if (userFilter.authorities().isEmpty()) {
            return new UserMngtVM(
                    userFilter.id(),
                    userFilter.email(),
                    userFilter.name(),
                    null,
                    userFilter.activated()
            );
        }
        return userFilter;
    }

}
