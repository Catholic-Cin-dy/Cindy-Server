package com.app.cindy.exception;

import com.app.cindy.constants.CommonResponseStatus;
import lombok.Getter;

@Getter
public class BadRequestException extends BaseException {
    private String message;

    public BadRequestException(String message) {
        super(CommonResponseStatus._BAD_REQUEST);
        this.message = message;
    }

    public BadRequestException(CommonResponseStatus errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public BadRequestException(CommonResponseStatus errorCode) {
        super(errorCode);
    }
}
