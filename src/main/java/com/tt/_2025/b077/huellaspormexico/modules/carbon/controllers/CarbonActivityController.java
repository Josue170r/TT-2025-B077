package com.tt._2025.b077.huellaspormexico.modules.carbon.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.carbon.entities.CarbonActivity;
import com.tt._2025.b077.huellaspormexico.modules.carbon.service.CarbonActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/carbon-activity")
@RequiredArgsConstructor
public class CarbonActivityController {

    private final CarbonActivityService carbonActivityService;

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getUserActivities(
            @PathVariable Long userId,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<CarbonActivity> activities = carbonActivityService.getUserActivities(userId, activityType, startDate, endDate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, activities));
    }

    @RequestMapping(path = "/total-co2/{userId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getUserTotalCo2(
            @PathVariable Long userId,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        BigDecimal totalCo2 = carbonActivityService.calculateUserTotalCo2(userId, activityType, startDate, endDate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Total de emisiones", totalCo2));
    }
}