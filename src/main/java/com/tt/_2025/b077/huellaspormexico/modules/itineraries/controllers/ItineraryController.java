package com.tt._2025.b077.huellaspormexico.modules.itineraries.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.*;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.Itinerary;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryDay;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryPlace;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.services.ItineraryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<Itinerary>> createItinerary(
            @Valid @RequestBody CreateItineraryDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        Itinerary itinerary = itineraryService.createCompleteItinerary(dto, username);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED, "Itinerario creado exitosamente", itinerary));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<Itinerary>> getItinerary(@PathVariable Long id) {
        Itinerary itinerary = itineraryService.getItineraryById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, itinerary));
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public Page<ItinerarySummaryDTO> getUserItineraries(
            Authentication authentication,
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        String username = authentication.getName();
        return itineraryService.getUserItineraries(username, pageable);
    }

    @RequestMapping(path = "/{id}/days", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<ItineraryDaysResponseDto>>> getItineraryDays(@PathVariable Long id) {
        List<ItineraryDaysResponseDto> days = itineraryService.getItineraryDays(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, days));
    }

    @RequestMapping(path = "/{itineraryId}/days/{dayId}/order", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<?>> updateVisitOrder(
            @PathVariable Long itineraryId,
            @PathVariable Long dayId,
            @Valid @RequestBody UpdateVisitOrderDTO dto) {

        ItineraryDay day = itineraryService.updateVisitOrder(itineraryId, dayId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Orden actualizado exitosamente", day));
    }

    @RequestMapping(path = "/{itineraryId}/days/{dayId}/places/{placeId}", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<ItineraryPlace>> updatePlace(
            @PathVariable Long itineraryId,
            @PathVariable Long dayId,
            @PathVariable Long placeId,
            @Valid @RequestBody UpdatePlaceDTO dto) {

        ItineraryPlace place = itineraryService.updatePlace(itineraryId, dayId, placeId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Lugar actualizado exitosamente", place));
    }

    @RequestMapping(path = "/{itineraryId}/days/{dayId}/places/add", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<ItineraryPlace>> addItineraryPlace(
            @PathVariable Long itineraryId,
            @PathVariable Long dayId,
            @Valid @RequestBody AddNewPlaceDTO dto) {

        itineraryService.addPlaceToDay(itineraryId, dayId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Se agregó el lugar al itinerario", null));
    }

    @RequestMapping(path = "/{itineraryId}/days/{dayId}/places/{placeId}/visit", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<?>> setPlaceVisited(
            @PathVariable Long itineraryId,
            @PathVariable Long dayId,
            @PathVariable Long placeId) {

        itineraryService.setPlaceVisited(itineraryId, dayId, placeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Se marcó el lugar como visitado"));
    }

    @RequestMapping(path = "/{itineraryId}/days/{dayId}/places/{placeId}/time", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<ItineraryPlace>> updatePlaceTime(
            @PathVariable Long itineraryId,
            @PathVariable Long dayId,
            @PathVariable Long placeId,
            @Valid @RequestBody UpdatePlaceTimeDTO dto) {

        ItineraryPlace updatedPlace = itineraryService.updatePlaceTime(itineraryId, dayId, placeId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Horario actualizado exitosamente", updatedPlace));
    }

    @RequestMapping(path = "/{itineraryId}/days/{dayId}/places/{placeId}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponse<?>> deletePlaceByDayId(
            @PathVariable Long itineraryId,
            @PathVariable Long dayId,
            @PathVariable Long placeId) {

        itineraryService.deletePlaceByDayId(itineraryId, dayId, placeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Lugar eliminado exitosamente"));
    }


    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteItinerary(@PathVariable Long id) {
        itineraryService.deleteItinerary(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Itinerario eliminado exitosamente"));
    }
}