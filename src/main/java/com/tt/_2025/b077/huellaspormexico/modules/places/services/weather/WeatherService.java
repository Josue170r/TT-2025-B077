package com.tt._2025.b077.huellaspormexico.modules.places.services.weather;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.weather.WeatherSummary;

public interface WeatherService {
    WeatherSummary getWeatherByCoordinates(Double latitude, Double longitude);
    WeatherSummary getWeatherByCity(String cityName);
}
