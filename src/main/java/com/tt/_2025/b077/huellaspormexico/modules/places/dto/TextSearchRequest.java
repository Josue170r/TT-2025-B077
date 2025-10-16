package com.tt._2025.b077.huellaspormexico.modules.places.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextSearchRequest {

    @NotBlank(message = "El texto de búsqueda es requerido")
    @Size(min = 2, max = 200, message = "El texto debe tener entre 2 y 200 caracteres")
    private String query;

    private String location;

    private Integer radius;

    private String type;

    @Builder.Default
    private String language = "es";

    @Builder.Default
    private String region = "mx";

    private Boolean openNow;
}