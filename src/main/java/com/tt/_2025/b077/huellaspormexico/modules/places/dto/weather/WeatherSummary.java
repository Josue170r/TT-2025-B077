package com.tt._2025.b077.huellaspormexico.modules.places.dto.weather;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherSummary {
    private String cityName;
    private Double temperature;
    private String temperatureFormatted;
    private String description;
    private String iconUrl;
    private Integer humidity;
    private String humidityFormatted;
    private String windSpeedFormatted;
}
