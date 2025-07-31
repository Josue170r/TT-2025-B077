package com.tt._2025.b077.huellaspormexico.modules.places.reporsitories;

import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {
    List<PlaceReview> findAllByPlaceId(Long placeId);
}
