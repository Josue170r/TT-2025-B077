package com.tt._2025.b077.huellaspormexico.modules.engagement.repositories;

import com.tt._2025.b077.huellaspormexico.modules.engagement.entities.PlaceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceHistoryRepository extends JpaRepository<PlaceHistory, Long> {
    Page<PlaceHistory> findAllByUserId(Long userId, Pageable pageable);
    Optional<PlaceHistory> findByUserIdAndPlaceId(Long userId, Long placeId);
}
