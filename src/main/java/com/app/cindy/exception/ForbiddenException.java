package com.app.cindy.exception;

import com.app.cindy.constants.CommonResponseStatus;
import lombok.Getter;

import static com.app.cindy.constants.CommonResponseStatus._BAD_REQUEST;


@Getter
public class ForbiddenException extends BaseException {
    private String message;

    public ForbiddenException(String message) {
        super(_BAD_REQUEST);
        this.message = message;
    }

    public ForbiddenException(CommonResponseStatus errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public ForbiddenException(CommonResponseStatus errorCode) {
        super(errorCode);
    }

}
