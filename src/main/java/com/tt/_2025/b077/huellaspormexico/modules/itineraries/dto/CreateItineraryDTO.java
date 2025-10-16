package com.tt._2025.b077.huellaspormexico.modules.itineraries.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateItineraryDTO {

    @NotBlank(message = "El título del viaje es obligatorio")
    private String tripTitle;

    @NotNull(message = "Debe seleccionar un hotel")
    private Long hotelPlaceId;

    private BigDecimal averageSustainableIndex;

    private Boolean isCertificatedHotel = false;

    @NotEmpty(message = "Debe tener al menos un día")
    @Valid
    private List<ItineraryDayDTO> itineraryDays;
}
