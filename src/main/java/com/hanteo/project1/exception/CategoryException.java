package com.hanteo.project1.exception;

import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;
    public CategoryException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}
