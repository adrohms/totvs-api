package com.totvs.application.account.dto;

public record UserAuthorityRec(
        Long userId,
        String authority_permission_name
) {
}
