package com.tt._2025.b077.huellaspormexico.modules.auth.exceptions;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseErrorCode("INVALID_TOKEN")
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidToken extends RuntimeException {
    public InvalidToken(String message) {
        super(message);
    }
}
