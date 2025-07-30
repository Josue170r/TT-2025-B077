package com.tt._2025.b077.huellaspormexico.modules.places.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NearBySearchRequest {

    @Builder.Default
    private int radius = 1500;
    @Builder.Default
    private String language = "es";

    @NotNull(message = "La latitude es obligatoria")
    private BigDecimal latitude;

    @NotNull(message = "La longitud es obligatoria")
    private BigDecimal longitude;

    @NotEmpty(message = "Debe especificar al menos un tipo de lugar")
    private List<String> types;
}
