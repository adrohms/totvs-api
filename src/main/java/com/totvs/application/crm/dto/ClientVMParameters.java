package com.totvs.application.crm.dto;

import com.totvs.application.common.dto.PersonRec;
import com.totvs.domain.crm.enumeration.InterestType;
import com.totvs.domain.crm.enumeration.OriginType;
import jakarta.validation.constraints.NotNull;

public record ClientVMParameters(
        Long id,
        String name,
        String email
) {
}