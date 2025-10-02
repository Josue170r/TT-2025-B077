package com.tt._2025.b077.huellaspormexico.modules.hotels.service;

import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CertificatedHotelService {
    CertificatedHotel findById(Long id);
    Page<CertificatedHotel> findHotelsBySearch(
            String hotelName,
            Long settlementId,
            Long stateId,
            List<Long> certificationIds,
            Double latitude,
            Double longitude,
            Pageable pageable);
}
