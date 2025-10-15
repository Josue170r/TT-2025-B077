package com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories;

import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItineraryDayRepository extends JpaRepository<ItineraryDay, Long> {
    Optional<ItineraryDay> findByIdAndItineraryId(Long dayId, Long itineraryId);
}
