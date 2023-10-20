package com.berna.rantemplate.global.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CommonErrorController  extends AbstractErrorController {
    public CommonErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    public ErrorResponse customError(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);
        return ErrorResponse.builder()
                        .code(httpStatus.value())
                        .message(httpStatus.getReasonPhrase()).build();
    }
}
