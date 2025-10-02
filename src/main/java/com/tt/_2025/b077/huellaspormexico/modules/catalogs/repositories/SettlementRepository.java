package com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.SettlementCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettlementRepository extends JpaRepository<SettlementCatalog, Long> {
    List<SettlementCatalog> findByStateId(Long stateId);
}
