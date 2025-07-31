package com.tt._2025.b077.huellaspormexico.modules.catalogs.controller;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CategoryPlacesCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.CategoryPlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogs")
public class CategoryPlaceController {

    private final CategoryPlaceService categoryPlaceService;

    public CategoryPlaceController(CategoryPlaceService categoryPlaceService) {
        this.categoryPlaceService = categoryPlaceService;
    }

    @RequestMapping(path = "/category-places", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getCategoryPlaces() {
        List<CategoryPlacesCatalog> categoryPlaces = categoryPlaceService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, categoryPlaces));
    }

}
