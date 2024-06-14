package com.totvs.application.common.mapper;

import com.totvs.application.account.mapper.EntityMapper;
import com.totvs.application.common.dto.PersonRec;
import com.totvs.domain.common.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonRec}.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper extends EntityMapper<PersonRec, Person> {

    @Mapping(source = "phones", target = "phones")
    @Mapping(source = "addresses", target = "addresses")
    PersonRec toDto(Person person);

    @Mapping(source = "phones", target = "phones")
    @Mapping(source = "addresses", target = "addresses")
    Person toEntity(PersonRec person);

}
