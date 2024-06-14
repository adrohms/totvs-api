package com.totvs.application.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRec(
        @NotNull
        @Size(min = 1, max = 50)
        String email,
        @NotNull
        @Size(min = 4, max = 100)
        String password,
        boolean rememberMe
) {
}
