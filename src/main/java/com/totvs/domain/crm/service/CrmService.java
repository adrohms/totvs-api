package com.totvs.domain.crm.service;

import com.totvs.application.common.exception.PhoneAlreadyUsedException;
import com.totvs.application.common.mapper.PersonMapper;
import com.totvs.application.crm.dto.ClientRec;
import com.totvs.application.crm.dto.ClientVMParameters;
import com.totvs.application.crm.mapper.ClientMapper;
import com.totvs.domain.common.repository.AddressRepository;
import com.totvs.domain.common.repository.PhoneRepository;
import com.totvs.domain.crm.model.Client;
import com.totvs.domain.crm.repository.CrmRepository;
import com.totvs.domain.crm.service.impl.CrmServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CrmService implements CrmServiceImpl {


    private final Logger log = LoggerFactory.getLogger(CrmService.class);

    private final ClientMapper clientMapper;
    private final CrmRepository crmRepository;
    private final PhoneRepository phoneRepository;
    private final AddressRepository addressRepository;
    private final PersonMapper personMapper;

    public CrmService(ClientMapper clientMapper,
                      CrmRepository crmRepository,
                      PhoneRepository phoneRepository, AddressRepository addressRepository, PersonMapper personMapper) {
        this.clientMapper = clientMapper;
        this.crmRepository = crmRepository;
        this.phoneRepository = phoneRepository;
        this.addressRepository = addressRepository;
        this.personMapper = personMapper;
    }

    @Override
    public ClientRec save(ClientRec clientRec) {
        log.debug("Request to save Person : {}", clientRec);

        clientRec.person().phones().stream().forEach(phone -> {
            if(!this.phoneRepository.findPhoneByNumber(phone.number()).isEmpty()) {
                throw new PhoneAlreadyUsedException();
            }
        });

        Client client = clientMapper.toEntity(clientRec);
        client = crmRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Override
    public ClientRec update(ClientRec clientRec) {
        log.debug("Request to update Person : {}", clientRec);
        Client client = clientMapper.toEntity(clientRec);
        client = crmRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Override
    public Optional<ClientRec> partialUpdate(ClientRec clientRec) {
        log.debug("Request to partially update Person : {}", clientRec);

        return crmRepository
                .findById(clientRec.id())
                .map(existingClient -> {
                    clientMapper.partialUpdate(existingClient, clientRec);

                    return existingClient;
                })
                .map(crmRepository::save)
                .map(clientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientRec> findAll() {
        log.debug("Request to get all People");
        return crmRepository.findAll().stream().map(clientMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientRec> findAllByFilter(ClientVMParameters clientVMParameters, Pageable pageable) {
        log.debug("Request to get all People");
        Page<ClientRec> clientRecs = crmRepository.findAllByFilter(clientVMParameters, pageable).map(clientMapper::toDto);
        return clientRecs;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ClientRec> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        return crmRepository.findById(id).map(clientMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        crmRepository.deleteById(id);
    }

}
