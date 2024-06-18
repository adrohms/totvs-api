package com.totvs.infrastructure.config;

import com.totvs.application.account.dto.UserRec;
import com.totvs.application.common.dto.AddressRec;
import com.totvs.application.common.dto.PersonRec;
import com.totvs.application.common.dto.PhoneRec;
import com.totvs.application.crm.dto.ClientRec;
import com.totvs.domain.account.repository.UserRepository;
import com.totvs.domain.account.service.AccountService;
import com.totvs.domain.common.enumeration.AddressType;
import com.totvs.domain.common.enumeration.PersonType;
import com.totvs.domain.common.enumeration.PhoneType;
import com.totvs.domain.crm.enumeration.InterestType;
import com.totvs.domain.crm.enumeration.OriginType;
import com.totvs.domain.crm.repository.CrmRepository;
import com.totvs.domain.crm.service.CrmService;
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
    private CrmService crmService;

    @Autowired
    private CrmRepository crmRepository;

    @Autowired
    private  UserRepository userRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        //TODO: Usuário padrão para teste. Deve ser removido em produção!!!
        if (userRepository.findOneByEmail("admin@totvs.com").isEmpty()) {

            PersonRec personRec = new PersonRec(
                    null,
                    "Totvs Admin",
                    "11223344555555",
                    PersonType.LEGAL_PERSON,
                    "admin@totvs.com",
                    Set.of(
                            new PhoneRec(null, "62981333333", PhoneType.BUSINESS),
                            new PhoneRec(null, "62984090909", PhoneType.BUSINESS)
                    ),
                    Set.of(
                            new AddressRec(null, "456 Multi Ln", "Central",
                                    "Techville", "TS", "Techland", "74300300",
                                    AddressType.BUSINESS, null, null)
                    )
            );

            UserRec userRec = new UserRec(
                    null,
                    "admin@totvs.com",
                    "admin1234",
                    true,
                    "es",
                    "https://example.com/admin.jpg",
                    Set.of("USER", "ADMIN"),
                    personRec
            );

            this.accountService.registerUser(userRec, userRec.password());
        }

        //TODO: Usuário padrão para teste. Deve ser removido em produção!!!
        if (userRepository.findOneByEmail("user@totvs.com").isEmpty()) {

            PersonRec personRec = new PersonRec(
                    null,
                    "Totvs User",
                    "51782000099",
                    PersonType.NATURAL_PERSON,
                    "user@totvs.com",
                    Set.of(
                            new PhoneRec(null, "62982000000", PhoneType.PERSONAL),
                            new PhoneRec(null, "62982111111", PhoneType.PERSONAL)
                    ),
                    Set.of(
                            new AddressRec(null, "456 Multi Ln", "Central",
                                    "Techville", "TS", "Techland", "74300300",
                                    AddressType.RESIDENTIAL, null, null)
                    )
            );

            UserRec userRec = new UserRec(
                    null,
                    "user@totvs.com",
                    "user1234",
                    true,
                    "es",
                    "https://example.com/user.jpg",
                    Set.of("USER"),
                    personRec
            );

            this.accountService.registerUser(userRec, userRec.password());
        }

        //TODO: Cliente padrão para teste. Deve ser removido em produção!!!
        if (!crmRepository.findClientByPersonEmail("adriel@totvs.com").isPresent()) {

            PersonRec personRec = new PersonRec(
                    null,
                    "Adriel Oliveira",
                    "01782000000",
                    PersonType.NATURAL_PERSON,
                    "adriel@totvs.com",
                    Set.of(
                            new PhoneRec(null, "62982201718", PhoneType.PERSONAL)
                    ),
                    Set.of(
                            new AddressRec(null, "C-53", "Setor Sudoeste",
                                    "Goiânia", "GO", "Brazil", "74305320",
                                    AddressType.RESIDENTIAL, null, null)
                    )
            );

            ClientRec clientRec = new ClientRec(
                    null,
                    OriginType.LINKEDIN,
                    InterestType.FINANCIAL,
                    personRec
            );

            this.crmService.save(clientRec);
        }

    }

}