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
    List<ItineraryDaysResponseDto> getItineraryDays(Long itineraryId);
    void addPlaceToDay(Long itineraryId, Long dayId, AddNewPlaceDTO dto);
    void updateVisitOrder(Long itineraryId, Long dayId, UpdateVisitOrderDTO dto);
    void setPlaceVisited(Long itineraryId, Long dayId, Long placeId);
    void updatePlace(Long itineraryId, Long dayId, Long placeId, UpdatePlaceDTO dto);
    void deletePlaceByDayId(Long itineraryId, Long dayId, Long placeId);
    void deleteItinerary(Long id);
}
