package com.tt._2025.b077.huellaspormexico.modules.catalogs.controller;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CategoryPlacesCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.StateCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.CategoryPlaceService;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.StateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogs")
public class CatalogController {

    private final CategoryPlaceService categoryPlaceService;
    private final StateService stateService;

    public CatalogController(CategoryPlaceService categoryPlaceService, StateService stateService) {
        this.categoryPlaceService = categoryPlaceService;
        this.stateService = stateService;
    }

    @RequestMapping(path = "/category-places", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getCategoryPlaces() {
        List<CategoryPlacesCatalog> categoryPlaces = categoryPlaceService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, categoryPlaces));
    }

    @RequestMapping(path = "/states", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getStates() {
        List<StateCatalog> states = stateService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null,states));
    }

    @RequestMapping(path = "/states/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getStateById(@PathVariable("id") Long id) {
        StateCatalog state = stateService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null,state));
    }

    @RequestMapping(path = "/states/search", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getStateByState(@RequestParam(value = "state") String state) {
        StateCatalog stateFound = stateService.findByState(state);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null,stateFound));
    }
}
