package com.totvs.application.account.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserMngtVM(
        Long id,
        @NotNull
        String email,
        String name,
        @NotNull
        Set<String> authorities,
        Boolean activated
) {
}
