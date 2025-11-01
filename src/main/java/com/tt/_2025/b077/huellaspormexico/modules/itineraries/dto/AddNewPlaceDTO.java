package com.tt._2025.b077.huellaspormexico.modules.itineraries.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AddNewPlaceDTO {
    @NotNull(message = "El nuevo lugar es obligatorio")
    private Long newPlaceId;

    private String postalCode;

    @NotNull(message = "La hora de llegada es obligatoria")
    private LocalTime arrivalTime;

    private LocalTime leavingTime;
}
