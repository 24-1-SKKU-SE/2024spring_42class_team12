package com.skku.fixskku.apipayload.exception;

import com.skku.fixskku.apipayload.ResponseStatus;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final ResponseStatus status;

    public GeneralException(ResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
