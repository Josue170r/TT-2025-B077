package com.tt._2025.b077.huellaspormexico.modules.itineraries.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ItineraryPlaceDTO {

    @NotNull(message = "El lugar es obligatorio")
    private Long placeId;

    private String postalCode;

    private Integer visitOrder;

    @NotNull(message = "La hora de llegada es obligatoria")
    private LocalTime arrivalTime;

    private LocalTime leavingTime;
}
