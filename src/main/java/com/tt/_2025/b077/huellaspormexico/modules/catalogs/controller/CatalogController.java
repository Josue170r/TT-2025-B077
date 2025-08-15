package com.tt._2025.b077.huellaspormexico.modules.catalogs.controller;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CategoryPlacesCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.PostalCodeCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.SettlementCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.StateCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.CategoryPlaceService;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.PostalCodeService;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.services.SettlementService;
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
    private final SettlementService settlementService;
    private final PostalCodeService postalCodeService;

    public CatalogController(
            CategoryPlaceService categoryPlaceService,
            StateService stateService,
            SettlementService settlementService,
            PostalCodeService postalCodeService) {
        this.categoryPlaceService = categoryPlaceService;
        this.stateService = stateService;
        this.settlementService = settlementService;
        this.postalCodeService = postalCodeService;
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

    @RequestMapping(path = "/settlements", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getSettlements() {
        List<SettlementCatalog> settlements = settlementService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null,settlements));
    }

    @RequestMapping(path = "/postal-codes", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getPostalCodes() {
        List<PostalCodeCatalog> postalCodes = postalCodeService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null,postalCodes));
    }

    @RequestMapping(path = "/postal-codes/search", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getPostalCodesBySearch(
            @RequestParam(required = false, value = "postal_code") String postalCode,
            @RequestParam(required = false, value = "settlement") Long settlementId,
            @RequestParam(required = false, value = "state") Long stateId
            ) {
        List<PostalCodeCatalog> postalCodes = postalCodeService.findBySearch(postalCode, settlementId, stateId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null,postalCodes));
    }
}
