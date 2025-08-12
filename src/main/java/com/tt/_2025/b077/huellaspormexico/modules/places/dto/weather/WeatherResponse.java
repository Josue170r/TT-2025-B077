package com.tt._2025.b077.huellaspormexico.modules.places.dto.weather;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WeatherResponse {

    private List<Weather> weather;
    private Main main;
    private Wind wind;
    private String name;

    @Data
    @NoArgsConstructor
    public static class Weather {
        private String description;
        private String icon;
    }

    @Data
    @NoArgsConstructor
    public static class Main {
        private Double temp;
        private Integer humidity;
    }

    @Data
    @NoArgsConstructor
    public static class Wind {
        private Double speed;
    }
}
