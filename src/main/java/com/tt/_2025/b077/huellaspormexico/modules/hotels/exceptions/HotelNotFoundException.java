package com.tt._2025.b077.huellaspormexico.modules.hotels.exceptions;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseErrorCode("HOTEL_NOT_FOUND")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(String message) {
        super(message);
    }
}
