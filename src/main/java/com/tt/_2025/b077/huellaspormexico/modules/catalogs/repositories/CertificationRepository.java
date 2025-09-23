package com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CertificationCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationRepository extends JpaRepository<CertificationCatalog, Long> {
    Optional<CertificationCatalog> findByCertification(String certification);
}
