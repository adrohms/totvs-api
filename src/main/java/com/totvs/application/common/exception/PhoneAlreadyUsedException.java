package com.totvs.application.common.exception;

import com.totvs.infrastructure.exception.BadRequestAlertException;

public class PhoneAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;
    private static final String message = "Este número de contato já foi utilizado!";

    public PhoneAlreadyUsedException() {
        super(CommonErrorConstants.PHONE_ALREADY_USED_TYPE, message, "phone", "phoneExists");
    }
}