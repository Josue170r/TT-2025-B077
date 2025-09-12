package com.tt._2025.b077.huellaspormexico.modules.catalogs.services;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CertificationCatalog;

import java.util.List;

public interface CertificationService {
    List<CertificationCatalog> getCertifications();
    CertificationCatalog getCertificationById(Long id);
    CertificationCatalog getCertificationByName(String certificationName);
}
