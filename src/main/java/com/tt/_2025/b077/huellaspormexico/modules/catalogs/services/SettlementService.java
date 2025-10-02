package com.tt._2025.b077.huellaspormexico.modules.catalogs.services;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.SettlementCatalog;

import java.util.List;

public interface SettlementService {
    List<SettlementCatalog> findAll(Long stateId);
}
