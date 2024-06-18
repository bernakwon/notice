package com.berna.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOTICE_NOT_FOUND(BAD_REQUEST, "해당 공지사항을 찾을 수 없습니다."),
    SIMULTANEOUS_UPDATE(INTERNAL_SERVER_ERROR, "동시 업데이트로 인해 저장을 실패했습니다."),

    FAIL_FILE_SAVE(INTERNAL_SERVER_ERROR, "파일 저장 실패"),

    FAIL_FILE_DELETE(INTERNAL_SERVER_ERROR, "파일 삭제 실패");

    private final HttpStatus httpStatus;
    private final String detail;

}
