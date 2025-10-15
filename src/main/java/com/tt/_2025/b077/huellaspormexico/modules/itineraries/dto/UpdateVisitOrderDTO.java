package com.tt._2025.b077.huellaspormexico.modules.itineraries.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateVisitOrderDTO {

    @NotNull(message = "Los IDs de los lugares son obligatorios")
    private List<Long> placeIds;
}
