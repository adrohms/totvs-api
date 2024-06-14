package com.totvs.application.account.mapper;

import com.totvs.application.account.dto.UserRec;
import com.totvs.application.common.mapper.PersonMapper;
import com.totvs.domain.account.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface UserMapper extends EntityMapper<UserRec, User> {

    @Mapping(source = "person", target = "person")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserRec toDto(User user);

    @Mapping(source = "person", target = "person")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRec userRec);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "password", ignore = true)
    void partialUpdate(@MappingTarget User user, UserRec dto);
}
