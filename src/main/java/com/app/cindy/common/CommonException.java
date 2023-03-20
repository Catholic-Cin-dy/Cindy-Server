package com.app.cindy.common;

import com.app.cindy.constants.CommonResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommonException extends Exception {
    private CommonResponseStatus status;
}
