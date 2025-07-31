package com.tt._2025.b077.huellaspormexico.modules.places.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceReviewRequest {

    @NotNull(message = "La fecha de la reseña es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewDate;

    @NotBlank(message = "El texto de la reseña no puede estar vacío")
    @Size(max = 2000, message = "El texto de la reseña no puede exceder los 2000 caracteres")
    private String text;

    @NotNull(message = "El rating es obligatorio")
    @DecimalMin(value = "0.0", message = "El rating debe ser mayor o igual a 0")
    @DecimalMax(value = "5.0", message = "El rating debe ser menor o igual a 5")
    @Digits(integer = 1, fraction = 2, message = "El rating debe tener máximo 1 dígito entero y 2 decimales")
    private BigDecimal rating;

    public Long getTimestamp() {
        if (reviewDate == null) {
            return null;
        }
        return reviewDate.getTime() / 1000L;
    }
}
