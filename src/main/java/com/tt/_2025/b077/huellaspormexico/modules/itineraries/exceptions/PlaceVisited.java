package com.tt._2025.b077.huellaspormexico.modules.itineraries.exceptions;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseErrorCode("PLACE_VISITED")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlaceVisited extends RuntimeException {
    public PlaceVisited(String message) {
        super(message);
    }
}
