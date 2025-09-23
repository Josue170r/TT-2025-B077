package com.tt._2025.b077.huellaspormexico.modules.catalogs.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CertificationCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.exceptions.CertificationNotFound;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.CertificationRepository;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.CertificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationServiceImpl implements CertificationService {

    private final CertificationRepository certificationRepository;

    public CertificationServiceImpl(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    @Override
    public List<CertificationCatalog> getCertifications() {
        return certificationRepository.findAll();
    }

    @Override
    public CertificationCatalog getCertificationById(Long id) {
        return certificationRepository.findById(id)
                .orElseThrow(() -> new CertificationNotFound("Certificación no encontrada"));
    }

    @Override
    public CertificationCatalog getCertificationByName(String certificationName) {
        return certificationRepository.findByCertification(certificationName)
                .orElseThrow(() -> new CertificationNotFound("Certificación no encontrada"));
    }
}
