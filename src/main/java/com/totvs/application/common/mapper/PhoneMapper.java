package com.totvs.application.common.mapper;

import com.totvs.application.account.mapper.EntityMapper;
import com.totvs.application.common.dto.PhoneRec;
import com.totvs.domain.common.model.Phone;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Phone} and its DTO {@link PhoneRec}.
 */
@Mapper(componentModel = "spring")
public interface PhoneMapper extends EntityMapper<PhoneRec, Phone> { }