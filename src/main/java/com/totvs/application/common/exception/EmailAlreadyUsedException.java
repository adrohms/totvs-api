package com.totvs.application.common.exception;

import com.totvs.infrastructure.exception.BadRequestAlertException;
import com.totvs.infrastructure.exception.ErrorConstants;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;
    private static final String message = "Este email já foi utilizado!";

    public EmailAlreadyUsedException() {
        super(CommonErrorConstants.EMAIL_ALREADY_USED_TYPE, message, "person", "emailExists");
    }
}
