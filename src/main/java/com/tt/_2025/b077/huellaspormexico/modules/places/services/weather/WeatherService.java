package com.tt._2025.b077.huellaspormexico.modules.places.services.weather;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.weather.WeatherSummary;

import java.math.BigDecimal;

public interface WeatherService {
    WeatherSummary getWeatherByCoordinates(BigDecimal lat, BigDecimal lng);
    WeatherSummary getWeatherByCity(String cityName);
}
