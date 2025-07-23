package com.tt._2025.b077.huellaspormexico.modules.auth.exceptions;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseErrorCode("USER_NOT_VERIFIED")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotVerified extends RuntimeException {
    public UserNotVerified(String message) {
        super(message);
    }
}
