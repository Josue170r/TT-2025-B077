package com.tt._2025.b077.huellaspormexico.modules.engagement.services;

import com.tt._2025.b077.huellaspormexico.modules.engagement.entities.PlaceFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoritesService {
    void toggleFavorite(String username, Long placeId);
    List<Long> getAllFavoritesIds(String username);
    Page<PlaceFavorite> getAllByUserId(String username, Pageable pageable);
}
