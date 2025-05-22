package com.svc.jogging.api.config;

import com.svc.jogging.api.util.RestHelper;
import com.svc.jogging.model.res.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> onGenericException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return generateResult(exception);
    }

    private ResponseEntity<Object> generateResult(Exception exception) {
        ResponseData res = new ResponseData();
        res.setException(exception);

        return RestHelper.createResponse(res);
    }
}
