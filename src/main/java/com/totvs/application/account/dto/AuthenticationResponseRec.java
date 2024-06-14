package com.totvs.application.account.dto;

import com.totvs.application.account.enumeration.AuthenticationStatus;

public record AuthenticationResponseRec(

    AuthenticationStatus status,
    String message

) {}
