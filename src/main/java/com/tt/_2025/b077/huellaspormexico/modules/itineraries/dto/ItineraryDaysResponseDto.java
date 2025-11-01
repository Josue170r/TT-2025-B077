package com.tt._2025.b077.huellaspormexico.modules.itineraries.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryDaysResponseDto {
    private Long itineraryDayId;
    private LocalDate itineraryDate;
}
