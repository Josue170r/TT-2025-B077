package com.tt._2025.b077.huellaspormexico.modules.places.controllers.weather;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.weather.WeatherSummary;
import com.tt._2025.b077.huellaspormexico.modules.places.services.weather.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping(path = "/coordinates", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getWeatherByCoordinates(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lng) {

        WeatherSummary weather = weatherService.getWeatherByCoordinates(lat, lng);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Clima obtenido correctamente", weather));
    }

    @RequestMapping(path = "/city", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>> getWeatherByCity(
            @RequestParam String name) {

        WeatherSummary weather = weatherService.getWeatherByCity(name);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Clima obtenido correctamente", weather));
    }
}
