package com.tt._2025.b077.huellaspormexico.modules.hotels.repositories;

import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificatedHotelRepository extends JpaRepository<CertificatedHotel, Long> {
    List<CertificatedHotel> findBySettlementId(Long settlementId);
}
