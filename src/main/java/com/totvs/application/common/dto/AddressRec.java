package com.totvs.application.common.dto;

import com.totvs.domain.common.enumeration.AddressType;

public record AddressRec(
    Long id,
    String street,
    String district,
    String city,
    String state,
    String country,
    String zipCode,
    AddressType addressType,
    String latitude,
    String longitude
) {
}
