package com.tt._2025.b077.huellaspormexico.modules.engagement.services;

import com.tt._2025.b077.huellaspormexico.modules.engagement.entities.PlaceFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoritesService {
    void toggleFavorite(String username, Long placeId);
    Page<PlaceFavorite> getAllByUserId(String username, Pageable pageable);
}
