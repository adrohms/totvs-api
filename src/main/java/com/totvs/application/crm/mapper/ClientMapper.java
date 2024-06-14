package com.totvs.application.crm.mapper;

import com.totvs.application.account.mapper.EntityMapper;
import com.totvs.application.crm.dto.ClientRec;
import com.totvs.domain.common.model.Person;
import com.totvs.domain.crm.model.Client;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Person} and its DTO {@link ClientRec}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientRec, Client> {
}
