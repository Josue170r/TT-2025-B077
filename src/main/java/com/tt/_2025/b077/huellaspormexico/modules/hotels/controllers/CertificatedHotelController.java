package com.tt._2025.b077.huellaspormexico.modules.hotels.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import com.tt._2025.b077.huellaspormexico.modules.hotels.service.CertificatedHotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificated-hotels")
public class CertificatedHotelController {

    private final CertificatedHotelService certificatedHotelService;

    public CertificatedHotelController(CertificatedHotelService certificatedHotelService) {
        this.certificatedHotelService = certificatedHotelService;
    }

    @RequestMapping(path = "/settlement/{settlementId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getHotelsBySettlement(@PathVariable("settlementId") Long settlementId) {
        List<CertificatedHotel> hotels = certificatedHotelService.findHotelsBySettlement(settlementId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, hotels));
    }
}