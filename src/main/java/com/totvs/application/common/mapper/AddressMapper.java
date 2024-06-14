package com.totvs.application.common.mapper;


import com.totvs.application.account.mapper.EntityMapper;
import com.totvs.application.common.dto.AddressRec;
import com.totvs.domain.common.model.Address;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressRec}.
 */
@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressRec, Address> { }
