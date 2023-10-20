package com.berna.rantemplate.global.common;

import com.berna.rantemplate.global.error.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonResponse<T> {
    private boolean result;
    private ErrorResponse error;
    private T data;
}
