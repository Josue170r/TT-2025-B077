package com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories;

import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByUserId(Long userId);
}
