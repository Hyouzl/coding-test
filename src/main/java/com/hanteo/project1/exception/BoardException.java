package com.hanteo.project1.exception;

public class BoardException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;
    public BoardException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}