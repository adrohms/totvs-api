package com.totvs.infrastructure.config;

import com.totvs.application.account.dto.UserRec;
import com.totvs.application.common.dto.AddressRec;
import com.totvs.application.common.dto.PersonRec;
import com.totvs.application.common.dto.PhoneRec;
import com.totvs.domain.account.repository.UserRepository;
import com.totvs.domain.account.service.AccountService;
import com.totvs.domain.common.enumeration.AddressType;
import com.totvs.domain.common.enumeration.PersonType;
import com.totvs.domain.common.enumeration.PhoneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private AccountService accountService;

    @Autowired
    private  UserRepository userRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        //TODO: Usuário padrão para teste. Deve ser removido em produção!!!
        if (userRepository.findOneByEmail("totvs@gmail.com").isEmpty()) {

            // Create Person
            PersonRec personRec = new PersonRec(
                    null,
                    "Mike Doe",
                    "112233445",
                    PersonType.NATURAL_PERSON,
                    "mikedoe@example.com",
                    Set.of(
                            new PhoneRec(null, "111-222-3333", PhoneType.PERSONAL),
                            new PhoneRec(null, "444-555-6666", PhoneType.BUSINESS)
                    ),
                    Set.of(
                            new AddressRec(null, "456 Multi Ln", "Central",
                                    "Techville", "TS", "Techland", "12345",
                                    AddressType.BUSINESS, null, null)
                    )
            );

            // Create User
            UserRec userRec = new UserRec(
                    null,
                    "totvs@gmail.com",
                    "Totvs789",
                    true,
                    "es",
                    "https://example.com/mikedoe.jpg",
                    Set.of("USER", "ADMIN"),
                    personRec
            );

            this.accountService.registerUser(userRec, userRec.password());
        }

    }

}