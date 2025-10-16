package com.tt._2025.b077.huellaspormexico.modules.itineraries.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.CreateItineraryDTO;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.UpdatePlaceDTO;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.UpdateVisitOrderDTO;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.Itinerary;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryDay;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryPlace;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.services.ItineraryService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<List<Itinerary>>> getUserItineraries(Authentication authentication) {
        String username = authentication.getName();
        List<Itinerary> itineraries = itineraryService.getUserItineraries(username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, itineraries));
    }

    @RequestMapping(path = "/{itineraryId}/days/{dayId}/order", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<?>> updateVisitOrder(
            @PathVariable Long itineraryId,
            @PathVariable Long dayId,
            @Valid @RequestBody UpdateVisitOrderDTO dto) {

        itineraryService.updateVisitOrder(itineraryId, dayId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Orden actualizado exitosamente", null));
    }

    @RequestMapping(path = "/{itineraryId}/days/{dayId}/places/{placeId}", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<ItineraryPlace>> updatePlace(
            @PathVariable Long itineraryId,
            @PathVariable Long dayId,
            @PathVariable Long placeId,
            @Valid @RequestBody UpdatePlaceDTO dto) {

        itineraryService.updatePlace(itineraryId, dayId, placeId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Lugar actualizado exitosamente", null));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteItinerary(@PathVariable Long id) {
        itineraryService.deleteItinerary(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.of(HttpStatus.NO_CONTENT, "Itinerario eliminado exitosamente"));
    }
}