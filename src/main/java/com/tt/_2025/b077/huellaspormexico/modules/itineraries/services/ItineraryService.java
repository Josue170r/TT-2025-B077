package com.tt._2025.b077.huellaspormexico.modules.itineraries.services;

import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.CreateItineraryDTO;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.UpdatePlaceDTO;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.UpdateVisitOrderDTO;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.Itinerary;

import java.util.List;

public interface ItineraryService {
    Itinerary createCompleteItinerary(CreateItineraryDTO dto, String username);
    Itinerary getItineraryById(Long id);
    List<Itinerary> getUserItineraries(String username);
    void updateVisitOrder(Long itineraryId, Long dayId, UpdateVisitOrderDTO dto);
    void updatePlace(Long itineraryId, Long dayId, Long placeId, UpdatePlaceDTO dto);
    void deleteItinerary(Long id);
}
