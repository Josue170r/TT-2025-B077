package com.tt._2025.b077.huellaspormexico.modules.itineraries.services;

import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.*;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.Itinerary;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryPlace;

import java.util.List;

public interface ItineraryService {
    Itinerary createCompleteItinerary(CreateItineraryDTO dto, String username);
    Itinerary getItineraryById(Long id);
    List<ItinerarySummaryDTO> getUserItineraries(String username);
    ItineraryPlace updatePlaceTime(Long itineraryId, Long dayId, Long placeId, UpdatePlaceTimeDTO dto);
    void updateVisitOrder(Long itineraryId, Long dayId, UpdateVisitOrderDTO dto);
    void updatePlace(Long itineraryId, Long dayId, Long placeId, UpdatePlaceDTO dto);
    void deleteItinerary(Long id);
}
