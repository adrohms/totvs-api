package com.totvs.application.common.dto;

import com.totvs.domain.common.enumeration.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PersonRec(
    Long id,
    @NotNull
    String name,
    @NotNull
    String taxId,
    @NotNull
    PersonType personType,
    @Email
    String email,
    Set<PhoneRec> phones,
    Set<AddressRec> addresses
) {
}
