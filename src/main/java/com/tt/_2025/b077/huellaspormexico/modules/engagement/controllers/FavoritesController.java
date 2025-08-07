package com.tt._2025.b077.huellaspormexico.modules.engagement.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.engagement.entities.PlaceFavorite;
import com.tt._2025.b077.huellaspormexico.modules.engagement.services.FavoritesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PlaceFavorite> getFavoritesByUser(
            Authentication authentication,
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return favoritesService.getAllByUserId(authentication.getName(), pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> addFavorite(
            Authentication authentication,
            @RequestParam(name = "place_id") Long placeId) {
        favoritesService.toggleFavorite(authentication.getName(), placeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null));
    }
}
