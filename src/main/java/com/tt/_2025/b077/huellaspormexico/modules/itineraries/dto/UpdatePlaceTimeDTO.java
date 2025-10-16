package com.tt._2025.b077.huellaspormexico.modules.itineraries.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdatePlaceTimeDTO {

    @NotNull(message = "La hora de llegada es obligatoria")
    private LocalTime arrivalTime;

    private LocalTime leavingTime;
}