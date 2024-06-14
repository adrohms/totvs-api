package com.totvs.domain.common.service;

import com.totvs.application.common.dto.PersonRec;
import com.totvs.application.common.mapper.PersonMapper;
import com.totvs.domain.common.model.Person;
import com.totvs.domain.common.repository.PersonRepository;
import com.totvs.domain.common.service.impl.PersonImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonService implements PersonImpl {


    private final Logger log = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public PersonRec save(PersonRec personRec) {
        log.debug("Request to save Person : {}", personRec);
        Person person = personMapper.toEntity(personRec);
        person = personRepository.save(person);
        return personMapper.toDto(person);
    }

    @Override
    public PersonRec update(PersonRec personRec) {
        log.debug("Request to update Person : {}", personRec);
        Person person = personMapper.toEntity(personRec);
        person = personRepository.save(person);
        return personMapper.toDto(person);
    }

    @Override
    public Optional<PersonRec> partialUpdate(PersonRec personRec) {
        log.debug("Request to partially update Person : {}", personRec);

        return personRepository
                .findById(personRec.id())
                .map(existingPerson -> {
                    personMapper.partialUpdate(existingPerson, personRec);

                    return existingPerson;
                })
                .map(personRepository::save)
                .map(personMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonRec> findAll() {
        log.debug("Request to get all People");
        return personRepository.findAll().stream().map(personMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonRec> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        return personRepository.findById(id).map(personMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.deleteById(id);
    }

}
