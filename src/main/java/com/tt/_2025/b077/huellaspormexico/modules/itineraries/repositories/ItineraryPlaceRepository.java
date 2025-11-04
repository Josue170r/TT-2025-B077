package com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories;

import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItineraryPlaceRepository extends JpaRepository<ItineraryPlace, Long> {
    List<ItineraryPlace> findByItineraryDayIdOrderByVisitOrder(Long itineraryDayId);
    List<ItineraryPlace> findByItineraryDayId(Long itineraryDayId);
    ItineraryPlace findFirstByItineraryDayIdOrderByVisitOrderDesc(Long itineraryDayId);
    Boolean existsByPlacePlaceIdAndItineraryDayId(String placeId, Long itineraryDay_id);
    Optional<ItineraryPlace> findByIdAndItineraryDayId(Long placeId, Long dayId);
}
