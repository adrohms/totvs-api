package com.totvs.domain.account.service;

import com.totvs.application.common.dto.PersonRec;
import com.totvs.application.common.exception.EmailAlreadyUsedException;
import com.totvs.application.common.mapper.PersonMapper;
import com.totvs.domain.account.model.Authority;
import com.totvs.domain.account.model.User;
import com.totvs.domain.account.repository.AuthorityRepository;
import com.totvs.domain.account.repository.UserRepository;
import com.totvs.application.account.dto.UserRec;
import com.totvs.application.account.mapper.UserMapper;
import com.totvs.domain.common.model.Person;
import com.totvs.domain.common.repository.PersonRepository;
import com.totvs.infrastructure.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final PersonRepository personRepository;
    private final UserMapper userMapper;
    private final PersonMapper personMapper;

    public AccountService(
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

    @Transactional
    public User registerUser(UserRec userRec, String password) {
        userRepository
                .findOneByEmail(userRec.email().toLowerCase())
                .ifPresent(existingUser -> {
                    throw new EmailAlreadyUsedException();
                });

        User newUser = this.userMapper.toEntity(userRec);
        newUser.setPassword(this.passwordEncoder.encode(password));
        newUser.setAuthorities(
                this.createSetOfAuthoritiesFromSetOfStrings(userRec.authorities())
        );

        Person newUserPerson = this.buildRegisterUserPerson(userRec.person());
        newUser.setPerson(newUserPerson);

        this.userRepository.save(newUser);

        return newUser;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByEmail);
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

    public Person buildRegisterUserPerson(PersonRec personRec) {
        Person newUserPerson = this.personMapper.toEntity(personRec);
        return newUserPerson;
    }

    public Set<Authority> createSetOfAuthoritiesFromSetOfStrings(Set<String> stringOfAuthorities) {
        Set<Authority> authorities = new HashSet<>();
        stringOfAuthorities.stream().forEach(authorityString -> {
            Authority authority = new Authority();
            authority.setPermissionName(authorityString);
            authorities.add(authority);
        });
        return authorities;
    }
}
