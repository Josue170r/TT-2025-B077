package com.tt._2025.b077.huellaspormexico.modules.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tt._2025.b077.huellaspormexico.modules.users.enums.UserRole;
import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(length = 500)
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @JsonIgnore
    @Builder.Default
    private UserRole role = UserRole.USER;

    @Override
    @JsonIgnore
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @PrePersist
    public void prePersist() {
        if (role == null) role = UserRole.USER;
    }
}