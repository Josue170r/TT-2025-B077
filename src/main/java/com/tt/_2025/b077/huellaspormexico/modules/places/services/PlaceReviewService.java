package com.tt._2025.b077.huellaspormexico.modules.places.services;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.PlaceReviewRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceReview;

import java.util.List;

public interface PlaceReviewService {
    List<PlaceReview> findAllByPlaceId(Long placeId);
    PlaceReview save(Long placeId, PlaceReviewRequest request, String username);
}
