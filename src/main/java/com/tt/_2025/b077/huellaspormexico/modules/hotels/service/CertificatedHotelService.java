package com.tt._2025.b077.huellaspormexico.modules.hotels.service;

import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;

import java.util.List;

public interface CertificatedHotelService {
    List<CertificatedHotel> findHotelsBySettlement(Long settlementId);
}
