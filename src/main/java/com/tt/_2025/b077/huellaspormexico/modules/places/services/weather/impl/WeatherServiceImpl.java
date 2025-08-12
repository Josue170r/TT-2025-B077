package com.tt._2025.b077.huellaspormexico.modules.places.services.weather.impl;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.weather.WeatherResponse;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.weather.WeatherSummary;
import com.tt._2025.b077.huellaspormexico.modules.places.services.weather.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.base.url}")
    private String baseUrl;

    @Value("${openweather.icon.url}")
    private String iconBaseUrl;

    public WeatherServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherSummary getWeatherByCoordinates(Double latitude, Double longitude) {

        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .queryParam("lang", "es")
                .build()
                .toUriString();

        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            return mapToWeatherSummary(response);
        } catch (Exception e) {
            log.error("Error fetching weather data for coordinates lat={}, lon={}: {}", latitude, longitude, e.getMessage());
            throw new RuntimeException("Error al obtener datos del clima: " + e.getMessage());
        }
    }

    @Override
    public WeatherSummary getWeatherByCity(String cityName) {

        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/weather")
                .queryParam("q", cityName)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .queryParam("lang", "es")
                .build()
                .toUriString();

        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            return mapToWeatherSummary(response);
        } catch (Exception e) {
            log.error("Error fetching weather data for city {}: {}", cityName, e.getMessage());
            throw new RuntimeException("Error al obtener datos del clima para la ciudad: " + e.getMessage());
        }
    }

    private WeatherSummary mapToWeatherSummary(WeatherResponse response) {
        if (response == null) {
            return null;
        }

        String description = response.getWeather() != null && !response.getWeather().isEmpty()
                ? response.getWeather().get(0).getDescription() : null;

        String iconCode = response.getWeather() != null && !response.getWeather().isEmpty()
                ? response.getWeather().get(0).getIcon() : null;

        String iconUrl = iconCode != null
                ? iconBaseUrl + "/" + iconCode + "@2x.png"
                : null;

        Double temperature = response.getMain() != null ? response.getMain().getTemp() : null;
        Integer humidity = response.getMain() != null ? response.getMain().getHumidity() : null;
        Double windSpeed = response.getWind() != null ? response.getWind().getSpeed() : null;

        return WeatherSummary.builder()
                .cityName(response.getName())
                .temperature(temperature)
                .temperatureFormatted(temperature != null ? String.format("%.1f°C", temperature) : null)
                .description(description)
                .iconUrl(iconUrl)
                .humidity(humidity)
                .humidityFormatted(humidity != null ? humidity + "%" : null)
                .windSpeedFormatted(windSpeed != null ? String.format("%.1f m/s", windSpeed) : null)
                .build();
    }
}
