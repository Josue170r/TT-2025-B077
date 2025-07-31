package com.tt._2025.b077.huellaspormexico.modules.places.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NearByPreferencesRequest {

    @Builder.Default
    private int radius = 1500;

    @Builder.Default
    private String language = "es";

    @NotNull(message = "La latitude es obligatoria")
    private BigDecimal latitude;

    @NotNull(message = "La longitud es obligatoria")
    private BigDecimal longitude;
}
