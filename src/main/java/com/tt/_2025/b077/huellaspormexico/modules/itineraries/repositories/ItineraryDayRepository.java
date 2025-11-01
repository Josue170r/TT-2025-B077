package com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories;

import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItineraryDayRepository extends JpaRepository<ItineraryDay, Long> {
    List<ItineraryDay> findByItineraryId(Long itineraryId);
    Optional<ItineraryDay> findByIdAndItineraryId(Long dayId, Long itineraryId);
}
