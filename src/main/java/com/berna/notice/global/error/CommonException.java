package com.berna.notice.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CommonException extends RuntimeException {
    private final ErrorCode errorCode;
}
