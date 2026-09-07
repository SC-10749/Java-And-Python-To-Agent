package com.example.adplatform.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(0, "success"),
    PARAM_ERROR(400, "parameter error"),
    NOT_FOUND(404, "resource not found"),
    BUSINESS_ERROR(422, "business error"),
    INTERNAL_ERROR(500, "internal server error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
