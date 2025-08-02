package com.tt._2025.b077.huellaspormexico.modules.users.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

    @Size(max = 50, message = "El nombre debe tener máximo 50 caracteres")
    private String name;

    @Size(max = 50, message = "El apellido paterno debe tener máximo 50 caracteres")
    private String lastName;

    @Size(max = 50, message = "El apellido materno debe tener máximo 50 caracteres")
    private String secondLastName;

    @Pattern(regexp = "^\\d{10}$", message = "El número de teléfono debe tener 10 dígitos")
    private String phoneNumber;
}
