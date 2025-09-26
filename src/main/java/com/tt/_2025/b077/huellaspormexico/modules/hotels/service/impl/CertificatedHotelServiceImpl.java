package com.tt._2025.b077.huellaspormexico.modules.hotels.service.impl;

import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import com.tt._2025.b077.huellaspormexico.modules.hotels.repositories.CertificatedHotelRepository;
import com.tt._2025.b077.huellaspormexico.modules.hotels.repositories.CertificatedHotelSpecification;
import com.tt._2025.b077.huellaspormexico.modules.hotels.service.CertificatedHotelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CertificatedHotelServiceImpl implements CertificatedHotelService {

    private final CertificatedHotelRepository certificatedHotelRepository;

    public CertificatedHotelServiceImpl(CertificatedHotelRepository certificatedHotelRepository) {
        this.certificatedHotelRepository = certificatedHotelRepository;
    }

    @Override
    public Page<CertificatedHotel> findHotelsBySearch(String hotelName, Long settlementId, Long stateId, Pageable pageable) {
        Specification<CertificatedHotel> spec = CertificatedHotelSpecification.buildSpecification(hotelName, settlementId, stateId);
        return certificatedHotelRepository.findAll(spec, pageable);
    }
}