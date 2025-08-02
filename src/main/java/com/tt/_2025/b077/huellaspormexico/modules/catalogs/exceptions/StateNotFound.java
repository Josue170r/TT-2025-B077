package com.tt._2025.b077.huellaspormexico.modules.catalogs.exceptions;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseErrorCode("STATE_NOT_FOUND")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StateNotFound extends RuntimeException {
    public StateNotFound(String message) {
        super(message);
    }
}
