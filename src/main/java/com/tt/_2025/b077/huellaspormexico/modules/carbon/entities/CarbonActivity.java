package com.tt._2025.b077.huellaspormexico.modules.carbon.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "carbon_activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarbonActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @NotNull(message = "El usuario es obligatorio")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false, precision = 10, scale = 4)
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad de CO2 debe ser mayor a 0")
    @NotNull(message = "La cantidad de CO2 generado es obligatoria")
    private BigDecimal co2Generated;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String unit = "KG";

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(length = 100)
    private String activityType;

    @Column(length = 500)
    private String description;
}