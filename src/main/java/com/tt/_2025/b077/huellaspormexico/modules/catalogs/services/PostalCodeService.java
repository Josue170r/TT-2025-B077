package com.tt._2025.b077.huellaspormexico.modules.catalogs.services;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.PostalCodeCatalog;

import java.util.List;

public interface PostalCodeService {
    List<PostalCodeCatalog> findAll();
    List<PostalCodeCatalog> findBySearch(String postalCode, Long settlementId, Long stateId);
}
