package com.tt._2025.b077.huellaspormexico.modules.catalogs.exceptions;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseErrorCode("CERTIFICATION_NOT_FOUND")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CertificationNotFound extends RuntimeException{
    public CertificationNotFound(String message) {
        super(message);
    }
}
