package com.tt._2025.b077.huellaspormexico.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties({"createdBy", "modifiedBy", "createdAt", "modifiedAt"})
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;
}
