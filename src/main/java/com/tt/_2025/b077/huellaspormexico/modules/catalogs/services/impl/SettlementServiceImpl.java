package com.tt._2025.b077.huellaspormexico.modules.catalogs.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.SettlementCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.SettlementRepository;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.SettlementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettlementServiceImpl implements SettlementService {

    private final SettlementRepository  settlementRepository;

    public SettlementServiceImpl(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    @Override
    public List<SettlementCatalog> findAll() {
        return settlementRepository.findAll();
    }
}
