package com.tt._2025.b077.huellaspormexico.controllers;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CategoryPlacesCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.CategoryPlacesRepository;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceImage;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/map")
public class MapController {

    private final PlaceRepository placeRepository;
    private final CategoryPlacesRepository categoryRepository;

    public MapController(PlaceRepository placeRepository, CategoryPlacesRepository categoryRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Value("${google.map.key}")
    private String googleMapKey;

    @GetMapping
    public String showMap(Model model) {
        List<CategoryPlacesCatalog> categories = categoryRepository.findAllByIsActiveTrue();

        List<Place> allPlaces = placeRepository.findAllByIsActiveTrue();

        model.addAttribute("categories", categories);
        model.addAttribute("places", allPlaces);
        model.addAttribute("googleMapKey", googleMapKey);

        return "maps/interactive-map";
    }

    @GetMapping("/places")
    @ResponseBody
    public ResponseEntity<List<PlaceDto>> getPlacesByCategory(
            @RequestParam(value = "categoryId", required = false) Long categoryId) {

        List<Place> places;

        if (categoryId != null) {
            places = findPlacesByCategory(categoryId);
        } else {
            places = placeRepository.findAllByIsActiveTrue();
        }

        List<PlaceDto> placeDtos = places.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(placeDtos);
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<PlaceDto>> searchPlaces(@RequestParam("query") String query) {
        List<Place> places = placeRepository.findByNameOrPlaceId(query.trim());

        List<PlaceDto> placeDtos = places.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(placeDtos);
    }

    private List<Place> findPlacesByCategory(Long categoryId) {
        return placeRepository.findByCategoryId(categoryId);
    }

    private PlaceDto convertToDto(Place place) {
        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .description(place.getDescription())
                .lat(place.getLat())
                .lng(place.getLng())
                .rating(place.getLocalRating())
                .formattedAddress(place.getFormattedAddress())
                .website(place.getWebsite())
                .googleMapsUrl(place.getGoogleMapsUrl())
                .formattedPhoneNumber(place.getFormattedPhoneNumber())
                .placeTypes(place.getPlaceTypes())
                .images(place.getImages())
                .build();
    }

    @lombok.Data
    @lombok.Builder
    public static class PlaceDto {
        private Long id;
        private String name;
        private String description;
        private double lat;
        private double lng;
        private double rating;
        private String formattedAddress;
        private String website;
        private String googleMapsUrl;
        private String formattedPhoneNumber;
        private List<String> placeTypes;
        private List<PlaceImage> images;
    }
}