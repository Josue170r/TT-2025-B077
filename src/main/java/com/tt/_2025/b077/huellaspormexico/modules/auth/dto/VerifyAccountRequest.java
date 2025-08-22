package com.tt._2025.b077.huellaspormexico.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyAccountRequest {

    @NotBlank(message = "El token es obligatorio")
    private String token;
}
