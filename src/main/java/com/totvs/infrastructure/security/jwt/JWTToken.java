package com.totvs.infrastructure.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object to return as body in JWT Authentication.
 */
public record JWTToken(
        @JsonProperty("id_token")
        String idToken
) {
}

