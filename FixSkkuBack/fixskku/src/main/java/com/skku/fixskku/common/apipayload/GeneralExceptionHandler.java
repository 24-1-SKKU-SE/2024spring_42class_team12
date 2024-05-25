package com.skku.fixskku.common.apipayload;

import com.skku.fixskku.common.apipayload.exception.GeneralException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<?> handleGeneralException(GeneralException ex) {
        return ResponseApi.of(ex.getStatus());
    }
}
