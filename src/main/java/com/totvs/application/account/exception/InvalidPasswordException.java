package com.totvs.application.account.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidPasswordException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;
    private static final String message = "Senha incorreta!";

    public InvalidPasswordException() {
        super(AccountErrorConstants.INVALID_PASSWORD_TYPE, message, Status.BAD_REQUEST);
    }
}
