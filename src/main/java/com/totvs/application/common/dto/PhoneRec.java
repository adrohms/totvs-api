package com.totvs.application.common.dto;

import com.totvs.domain.common.enumeration.PhoneType;

public record PhoneRec(
        Long id,
        String number,
        PhoneType phoneType
) {
}
