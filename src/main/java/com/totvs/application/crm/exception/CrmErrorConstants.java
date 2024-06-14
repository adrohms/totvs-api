package com.totvs.application.crm.exception;

import java.net.URI;

public final class CrmErrorConstants {

    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.totvs.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");


    private CrmErrorConstants() {}
}
