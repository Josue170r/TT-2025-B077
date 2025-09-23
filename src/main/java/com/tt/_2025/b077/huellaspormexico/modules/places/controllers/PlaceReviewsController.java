package com.tt._2025.b077.huellaspormexico.modules.places.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.PlaceReviewRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceReview;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class PlaceReviewsController {

    private final PlaceReviewService reviewService;

    public PlaceReviewsController(PlaceReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RequestMapping(path = "/{placeId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getReviewsById(@PathVariable Long placeId) {
        List<PlaceReview> reviews = reviewService.findAllByPlaceId(placeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, reviews));
    }

    @RequestMapping(path = "/{placeId}", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> saveReview(
            @PathVariable Long placeId,
            @Valid @RequestBody PlaceReviewRequest request,
            Authentication auth) {
        PlaceReview review = reviewService.save(placeId, request, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Reseña guardada correctamente", review));
    }
}
