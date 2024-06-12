package com.berna.notice.global.common;

import com.berna.notice.global.error.ErrorResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.TreeMap;

@RestControllerAdvice
public class CommonResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // Swagger 제외
        if (body instanceof TreeMap || request.getURI().getPath().equals("/v3/api-docs")) {

            return body;
        }
        //Error는 Error정보를 담아 return
        else if (body instanceof ErrorResponse) {
            ErrorResponse errorResponse = (ErrorResponse) body;
            return CommonResponse.builder()
                    .result(false)
                    .error(errorResponse)
                    .build();
        } else {

            return CommonResponse.builder()
                    .result(true)
                    .data(body)
                    .build();
        }
    }
}
