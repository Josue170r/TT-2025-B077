package com.tt._2025.b077.huellaspormexico.modules.engagement.services;

import com.tt._2025.b077.huellaspormexico.modules.engagement.entities.PlaceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService {
    void toggleHistory(String username, Long placeId);
    Page<PlaceHistory> getAllByUserId(String username, Pageable pageable);
}
