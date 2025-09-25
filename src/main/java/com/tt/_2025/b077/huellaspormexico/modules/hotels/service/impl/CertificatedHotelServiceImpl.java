package com.tt._2025.b077.huellaspormexico.modules.hotels.service.impl;

import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import com.tt._2025.b077.huellaspormexico.modules.hotels.repositories.CertificatedHotelRepository;
import com.tt._2025.b077.huellaspormexico.modules.hotels.service.CertificatedHotelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificatedHotelServiceImpl implements CertificatedHotelService {

    private final CertificatedHotelRepository certificatedHotelRepository;

    public CertificatedHotelServiceImpl(CertificatedHotelRepository certificatedHotelRepository) {
        this.certificatedHotelRepository = certificatedHotelRepository;
    }

    @Override
    public List<CertificatedHotel> findHotelsBySettlement(Long settlementId) {
        return certificatedHotelRepository.findBySettlementId(settlementId);
    }
}