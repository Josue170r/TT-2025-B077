package com.tt._2025.b077.huellaspormexico.modules.users.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 30)
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String lastName;

    @Size(max = 50)
    private String secondLastName;

    @Pattern(regexp = "^\\d{10}$", message = "El número de teléfono debe tener 10 dígitos")
    @Column(length = 10)
    private String phoneNumber;

    @Builder.Default
    private boolean isVerified = false;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "Debe tener entre 8 y 100 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|\\[\\]:\";'<>?,./]).+$",
            message = "Incluye mayúscula, minúscula, número y símbolo"
    )
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;

    public void createUserProfile() {
        UserProfile newProfile = new UserProfile();
        this.setUserProfile(newProfile);
        newProfile.setUser(this);
    }
}
