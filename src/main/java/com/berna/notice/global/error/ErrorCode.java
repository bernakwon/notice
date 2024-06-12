package com.berna.notice.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    NOT_FOUND_SAVING(NOT_FOUND, "적립내역이 없습니다."),
    NOT_FOUND_POINT(NOT_FOUND, "해당 정보로 저장된 적립금이 없습니다."),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    NOT_USE_POINT(BAD_REQUEST, "취소가능한 적립금이 아닙니다."),
    LACK_POINT(BAD_REQUEST, "적립금이 부족합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String detail;

}
