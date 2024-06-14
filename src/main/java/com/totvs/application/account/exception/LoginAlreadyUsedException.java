package com.totvs.application.account.exception;

import com.totvs.infrastructure.exception.BadRequestAlertException;
import com.totvs.infrastructure.exception.ErrorConstants;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;
    private static final String message = "Este login já foi utilizado!";

    public LoginAlreadyUsedException() {
        super(AccountErrorConstants.LOGIN_ALREADY_USED_TYPE, message, "user", "loginExists");
    }
}
