package com.hanteo.project1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 공통
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "Json 변환 중 오류 발생"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 오류가 발생했습니다."),

    // 카테고리
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4001", "카테고리를 찾을 수 없습니다."),
    CATEGORY_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "CATEGORY4004", "잘못된 카테고리 요청입니다."),
    DUPLICATED_CATEGORY(HttpStatus.NOT_ACCEPTABLE, "CATEGORY4003", "중복된 카테고리입니다."),

    // 게시판
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD4001", "게시판을 찾을 수 없습니다."),
    DUPLICATED_BOARD(HttpStatus.NOT_ACCEPTABLE, "BOARD4003", "중복된 게시판입니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }


}
