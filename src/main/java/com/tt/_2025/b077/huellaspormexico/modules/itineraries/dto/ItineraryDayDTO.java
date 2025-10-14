package com.tt._2025.b077.huellaspormexico.modules.itineraries.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ItineraryDayDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate itineraryDate;

    @NotEmpty(message = "Cada día debe tener al menos un lugar")
    @Valid
    private List<ItineraryPlaceDTO> places;
}