package com.tt._2025.b077.huellaspormexico.modules.engagement.repositories;

import com.tt._2025.b077.huellaspormexico.modules.engagement.entities.PlaceFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<PlaceFavorite, Long> {
    List<PlaceFavorite> findAllByUserId(Long userId);
    Page<PlaceFavorite> findAllByUserId(Long userId, Pageable pageable);
    Optional<PlaceFavorite> findByUserIdAndPlaceId(Long userId, Long placeId);
}
