package com.tt._2025.b077.huellaspormexico.modules.catalogs.entities;

import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Data
@Entity
@Table(name = "certifications")
@AllArgsConstructor
@NoArgsConstructor
public class CertificationCatalog extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certification", unique = true, nullable = false, length = 150)
    private String certification;

    @Column(name = "institution_website", length = 1000)
    private String institutionWebsite;
}