package com.tt._2025.b077.huellaspormexico.modules.engagement.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.engagement.entities.PlaceHistory;
import com.tt._2025.b077.huellaspormexico.modules.engagement.services.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PlaceHistory> getHistoryByUser(
            Authentication authentication,
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return historyService.getAllByUserId(authentication.getName(), pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> addHistory(
            Authentication authentication,
            @RequestParam(name = "place_id") Long placeId) {
        historyService.toggleHistory(authentication.getName(), placeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null));
    }
}
