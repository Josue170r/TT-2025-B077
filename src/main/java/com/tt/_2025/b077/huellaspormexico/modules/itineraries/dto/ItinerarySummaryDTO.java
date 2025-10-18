package com.tt._2025.b077.huellaspormexico.modules.itineraries.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItinerarySummaryDTO {

    private Long id;
    private String tripTitle;
    private BigDecimal averageSustainableIndex;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalPlaces;
    private List<String> images;
}