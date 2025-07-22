package com.tt._2025.b077.huellaspormexico.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El email o username es obligatorio")
    private String emailOrUsername;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}