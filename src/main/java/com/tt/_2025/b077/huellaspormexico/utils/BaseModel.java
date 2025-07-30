package com.tt._2025.b077.huellaspormexico.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * BaseModel acts as an abstract base class from which every
 * other model in the project will inherit. This class provides
 * every table with the following attributes:
 * + is_active (Boolean): Indicates if the object is active.
 * + created_at (DateTime): Store the datetime the object was created.
 * + modified_at (DateTime): Store the last datetime the object was modified.
 * + created_by (Long): Reference to the user who created the record.
 * + modified_by (Long): Reference to the user who last modified the record.
 */
@Slf4j
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties({"createdBy", "modifiedBy", "createdAt", "modifiedAt"})
public abstract class BaseModel {

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();

        String currentUsername = getCurrentUsername();
        if (currentUsername != null) {
            modifiedBy = currentUsername;
        }
    }

    /**
     * Gets user username from authentication context
     * @return user username or null
     */
    private String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal())) {
                return null;
            }
            return authentication.getName();

        } catch (Exception e) {
            log.error("Error al obtener el usuario: {}", e.getMessage());
        }
        return null;
    }
}
