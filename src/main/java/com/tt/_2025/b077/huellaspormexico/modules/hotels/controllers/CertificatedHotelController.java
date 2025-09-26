package com.tt._2025.b077.huellaspormexico.modules.hotels.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import com.tt._2025.b077.huellaspormexico.modules.hotels.service.CertificatedHotelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificated-hotels")
public class CertificatedHotelController {

    private final CertificatedHotelService certificatedHotelService;

    public CertificatedHotelController(CertificatedHotelService certificatedHotelService) {
        this.certificatedHotelService = certificatedHotelService;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getHotelsBySearch(
            @RequestParam(required = false, value = "hotel_name") String hotelName,
            @RequestParam(required = false, value = "settlement") Long settlementId,
            @RequestParam(required = false, value = "state") Long stateId,
            @PageableDefault(
                    sort = {"place.rating"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        Page<CertificatedHotel> hotels = certificatedHotelService.findHotelsBySearch(hotelName, settlementId, stateId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, hotels));
    }
}