package com.totvs.domain.crm.service;

import com.totvs.application.common.dto.AddressRec;
import com.totvs.application.common.dto.PersonRec;
import com.totvs.application.common.dto.PhoneRec;
import com.totvs.application.common.exception.PhoneAlreadyUsedException;
import com.totvs.application.crm.dto.ClientRec;
import com.totvs.application.crm.mapper.ClientMapper;
import com.totvs.domain.common.enumeration.AddressType;
import com.totvs.domain.common.enumeration.PersonType;
import com.totvs.domain.common.enumeration.PhoneType;
import com.totvs.domain.common.model.Phone;
import com.totvs.domain.common.repository.PhoneRepository;
import com.totvs.domain.crm.enumeration.InterestType;
import com.totvs.domain.crm.enumeration.OriginType;
import com.totvs.domain.crm.model.Client;
import com.totvs.domain.crm.repository.CrmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CrmServiceTest {

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private CrmRepository crmRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private CrmService crmService;

    @Test
    public void testSaveClientWithUniquePhoneNumber() {
        PhoneRec phoneRec = new PhoneRec(null, "123456789", PhoneType.PERSONAL);
        PhoneRec phoneRec2 = new PhoneRec(null, "123456789", PhoneType.BUSINESS);
        AddressRec addressRec = new AddressRec(null, "Main St", "Downtown", "City", "State",
                "Country", "12345", AddressType.BUSINESS, "10.0000", "20.0000");
        PersonRec personRec = new PersonRec(null, "John Doe", "12345678901", PersonType.NATURAL_PERSON,
                "john.doe@example.com", Set.of(phoneRec, phoneRec2), Set.of(addressRec));
        ClientRec clientRec = new ClientRec(null, OriginType.OTHER, InterestType.OTHER, personRec);

        Client clientEntity = new Client();
        ClientRec clientRecSaved = new ClientRec(1L, OriginType.OTHER, InterestType.OTHER, personRec);

        when(phoneRepository.findPhoneByNumber("123456789")).thenReturn(null);
        when(clientMapper.toEntity(clientRec)).thenReturn(clientEntity);
        when(crmRepository.save(clientEntity)).thenReturn(clientEntity);
        when(clientMapper.toDto(clientEntity)).thenReturn(clientRecSaved);

        ClientRec result = crmService.save(clientRec);

        assertEquals(phoneRepository.findPhoneByNumber("123456789"), null);
        assertNotEquals(result, null);
        assertEquals(result.id(), 1L);
    }

    @Test()
    public void testSaveClientWithDuplicatePhoneNumber() {
        PhoneRec phoneRec = new PhoneRec(null, "123456789", PhoneType.PERSONAL);
        PersonRec personRec = new PersonRec(null, "John Doe", "12345678901",
                PersonType.NATURAL_PERSON, "john.doe@example.com", Set.of(phoneRec), Collections.emptySet());
        ClientRec clientRec = new ClientRec(null, OriginType.OTHER, InterestType.OTHER, personRec);

        when(phoneRepository.findPhoneByNumber("123456789")).thenReturn(new Phone());

        assertThatThrownBy(() -> crmService.save(clientRec))
                .isInstanceOf(PhoneAlreadyUsedException.class);
    }

    @Test
    public void testUpdateClientWithUniquePhone() {

        PhoneRec phoneRec = new PhoneRec(1L, "123456789", PhoneType.PERSONAL);
        AddressRec addressRec = new AddressRec(1L, "Main St", "Downtown", "City", "State",
                "Country", "12345", AddressType.BUSINESS, "10.0000", "20.0000");
        PersonRec personRec = new PersonRec(1L, "John Doe", "12345678901", PersonType.NATURAL_PERSON,
                "john.doe@example.com", Set.of(phoneRec), Set.of(addressRec));

        ClientRec clientToUpdate = new ClientRec(1L, OriginType.OTHER, InterestType.OTHER, personRec);
        Client clientToUpdateEntity = new Client();
        Client clientUpdatedEntity = new Client();

        when(clientMapper.toEntity(clientToUpdate)).thenReturn(clientToUpdateEntity);
        when(crmRepository.save(clientToUpdateEntity)).thenReturn(clientUpdatedEntity);
        when(clientMapper.toDto(clientUpdatedEntity)).thenReturn(clientToUpdate);

        ClientRec result = crmService.update(clientToUpdate);

        assertEquals(clientToUpdate, result);
    }

    @Test()
    public void testUpdateClientWithDuplicatePhone() {
        Phone existingPhone = new Phone();
        existingPhone.setId(2L);
        existingPhone.setNumber("123456789");

        PhoneRec phoneRec = new PhoneRec(1L, "123456789", PhoneType.PERSONAL);
        AddressRec addressRec = new AddressRec(1L, "Main St", "Downtown", "City", "State",
                "Country", "12345", AddressType.BUSINESS, "10.0000", "20.0000");
        PersonRec personRec = new PersonRec(1L, "John Doe", "12345678901", PersonType.NATURAL_PERSON,
                "john.doe@example.com", Set.of(phoneRec), Set.of(addressRec));

        ClientRec clientToUpdate = new ClientRec(1L, OriginType.OTHER, InterestType.OTHER, personRec);

        when(phoneRepository.findPhoneByNumber("123456789")).thenReturn(existingPhone);
        assertThatThrownBy(() -> crmService.update(clientToUpdate))
                .isInstanceOf(PhoneAlreadyUsedException.class);
    }

}