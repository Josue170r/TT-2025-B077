package com.tt._2025.b077.huellaspormexico.modules.hotels.service.impl;

import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import com.tt._2025.b077.huellaspormexico.modules.hotels.exceptions.HotelNotFoundException;
import com.tt._2025.b077.huellaspormexico.modules.hotels.repositories.CertificatedHotelRepository;
import com.tt._2025.b077.huellaspormexico.modules.hotels.repositories.CertificatedHotelSpecification;
import com.tt._2025.b077.huellaspormexico.modules.hotels.service.CertificatedHotelService;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.enums.FetchMode;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CertificatedHotelServiceImpl implements CertificatedHotelService {

    private final CertificatedHotelRepository certificatedHotelRepository;
    private final PlaceApiService placeApiService;
    private final PlaceRepository placeRepository;

    public CertificatedHotelServiceImpl(
            CertificatedHotelRepository certificatedHotelRepository,
            PlaceApiService placeApiService,
            PlaceRepository placeRepository) {
        this.certificatedHotelRepository = certificatedHotelRepository;
        this.placeApiService = placeApiService;
        this.placeRepository = placeRepository;
    }

    @Override
    @Transactional
    public Page<CertificatedHotel> findHotelsBySearch(
            String hotelName,
            Long settlementId,
            Long stateId,
            List<Long> certificationIds,
            Double latitude,
            Double longitude,
            Pageable pageable) {
        Specification<CertificatedHotel> spec = CertificatedHotelSpecification.buildSpecification(
                hotelName, settlementId, stateId, certificationIds, latitude, longitude);
        Page<CertificatedHotel> hotels = certificatedHotelRepository.findAll(spec, pageable);

        hotels.getContent().stream()
                .map(CertificatedHotel::getPlace)
                .filter(place -> place.getFormattedAddress() == null || place.getFormattedAddress().isEmpty())
                .forEach(place -> {
                    try {
                        Place updatedPlace = placeApiService.fetchPlaceDetails(
                                place.getPlaceId(),
                                FetchMode.LIGHT
                        );

                        updatedPlace.setId(place.getId());

                        place.getReviews().stream()
                                .filter(review -> "Huellas por México".equals(review.getOrigin()))
                                .forEach(updatedPlace::addReview);

                        placeRepository.save(updatedPlace);

                    } catch (Exception e) {
                        log.warn("Could not fetch place details for {}: {}",
                                place.getPlaceId(), e.getMessage());
                    }
                });
            return hotels;
        }


    @Override
    public CertificatedHotel findById(Long id) {
        return certificatedHotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel no encontrado"));
    }
}