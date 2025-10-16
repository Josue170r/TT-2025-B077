package com.tt._2025.b077.huellaspormexico.modules.hotels.repositories;

import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CertificatedHotelRepository extends JpaRepository<CertificatedHotel, Long>, JpaSpecificationExecutor<CertificatedHotel> {
    Optional<CertificatedHotel> findByPlaceId(Long placeId);
}