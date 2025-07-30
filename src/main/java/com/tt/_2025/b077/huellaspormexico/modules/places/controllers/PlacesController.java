package com.tt._2025.b077.huellaspormexico.modules.places.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearBySearchRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.PlaceIdsRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/place")
public class PlacesController {

    private final PlaceService placeService;

    public PlacesController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @RequestMapping(path = "/details", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getPlaceDetails(@RequestParam String place_id) {
        Place place = placeService.getPlaceDetails(place_id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, place));
    }

    @RequestMapping(path = "/nearbysearch", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<Long>>> getNearBySearchPlacesIds(@Valid @RequestBody NearBySearchRequest request) {
        List<Long> response = placeService.getNearBySearchPlaces(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, response));
    }

    @RequestMapping(path = "/allById", method = RequestMethod.GET)
    public Page<Place> getAllPlaces(
            @RequestBody PlaceIdsRequest placeIds,
            @PageableDefault(
                    sort = {"rating", "name"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return placeService.getAllByIdList(placeIds.getPlaceIds(), pageable);
    }
}
