package com.tt._2025.b077.huellaspormexico.modules.places.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.*;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/place")
public class PlacesController {

    private final PlaceService placeService;

    public PlacesController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @RequestMapping(path = "/details", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getPlaceDetails(@RequestParam(name = "place_id") String placeId) {
        Place place = placeService.getPlaceDetails(placeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, place));
    }

    @RequestMapping(path = "/{placeId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getPlaceById(@PathVariable Long placeId) {
        Place place = placeService.getPlaceById(placeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, place));
    }

    @RequestMapping(path = "/photo", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getGooglePhoto(
            @RequestParam String photoReference,
            @RequestParam(defaultValue = "4000") int width) {
        byte[] imageBytes = placeService.getGooglePhoto(photoReference, width);
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(imageBytes);
    }

    @GetMapping("/proxy-image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .header("Cache-Control", "public, max-age=2592000")
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(path = "/nearby-search", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<List<Long>>> getNearBySearchPlacesIds(@Valid @RequestBody NearBySearchRequest request) {
        List<Long> response = placeService.getNearBySearchPlaces(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, response));
    }

    @RequestMapping(path = "/nearby-preferences", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> getNearByPreferences(
            @Valid @RequestBody NearByPreferencesRequest request,
            Authentication auth) {
        List<Long> response = placeService.getNearByPreferences(auth.getName(), request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, response));
    }

    @RequestMapping(path = "/search-by-name", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> getSearchByName(@Valid @RequestBody SearchByNameRequest request) {
        List<SearchByNameResponse> suggestions = placeService.searchPlacesByName(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, suggestions));
    }

    @RequestMapping(path = "/search-by-text", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> getSearchByText(@Valid @RequestBody TextSearchRequest request) {
        List<SearchByNameResponse> places = placeService.searchPlacesByText(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, places));
    }

    @RequestMapping(path = "/allById", method = RequestMethod.POST)
    public Page<Place> getAllPlaces(
            @RequestBody PlaceIdsRequest placeIds,
            @PageableDefault(
                    sort = {"rating"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return placeService.getAllByIdList(placeIds.getPlaceIds(), pageable);
    }
}
