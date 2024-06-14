package com.totvs.application.account.dto;

import com.totvs.application.account.constants.AccountConstants;
import com.totvs.application.common.dto.PersonRec;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserRec(
        Long id,
        @NotNull
        String email,
        @NotNull
        @Size(min = AccountConstants.PASSWORD_MIN_LENGTH, max = AccountConstants.PASSWORD_MAX_LENGTH)
        String password,
        Boolean activated,
        String langKey,
        String imageUrl,
        @NotNull
        Set<String> authorities,
        @NotNull
        PersonRec person
) {
}
