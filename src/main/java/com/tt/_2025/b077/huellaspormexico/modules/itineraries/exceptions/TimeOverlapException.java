package com.tt._2025.b077.huellaspormexico.modules.itineraries.exceptions;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseErrorCode("TIME_OVERLAP")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TimeOverlapException extends RuntimeException {
    public TimeOverlapException(String message) {
        super(message);
    }
}
