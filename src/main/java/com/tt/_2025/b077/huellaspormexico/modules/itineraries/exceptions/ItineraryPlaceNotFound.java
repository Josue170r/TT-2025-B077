package com.tt._2025.b077.huellaspormexico.modules.itineraries.exceptions;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseErrorCode("PLACE_NOT_FOUND")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItineraryPlaceNotFound extends RuntimeException {
    public ItineraryPlaceNotFound(String message) {
        super(message);
    }
}
