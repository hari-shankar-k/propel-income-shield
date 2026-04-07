package com.guide_wire.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherData getWeatherCode(double lat, double lon) {

        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?lat="
                    + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric";

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                return getDefaultWeather();
            }

            // 🌡️ temperature & humidity
            Map<String, Object> main = (Map<String, Object>) response.get("main");

            double temperature = main != null && main.get("temp") != null
                    ? ((Number) main.get("temp")).doubleValue()
                    : 30;

            double humidity = main != null && main.get("humidity") != null
                    ? ((Number) main.get("humidity")).doubleValue()
                    : 70;

            // 🌦️ weather
            List<Map<String, Object>> weatherList =
                    (List<Map<String, Object>>) response.get("weather");

            String weatherMain = "Clouds";

            if (weatherList != null && !weatherList.isEmpty()) {
                weatherMain = weatherList.get(0).get("main").toString();
            }

            int weatherCode = mapWeather(weatherMain);

            return new WeatherData(temperature, humidity, weatherCode);

        } catch (Exception e) {
            log.warn("Weather API failed: {}", e.getMessage());
            return getDefaultWeather();
        }
    }

    private WeatherData getDefaultWeather() {
        return new WeatherData(30, 70, 2);
    }

    private int mapWeather(String weather) {
        return switch (weather) {
            case "Clear" -> 0;
            case "Rain", "Drizzle" -> 1;
            case "Clouds" -> 2;
            case "Thunderstorm" -> 3;
            default -> 2;
        };
    }
    public WeatherData getWeatherData(double lat, double lon) {

        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?lat="
                    + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric";

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                return getDefaultWeather();
            }

            // 🌡 temperature & humidity
            Map<String, Object> main = (Map<String, Object>) response.get("main");

            double temperature = main.get("temp") != null
                    ? ((Number) main.get("temp")).doubleValue()
                    : 30;

            double humidity = main.get("humidity") != null
                    ? ((Number) main.get("humidity")).doubleValue()
                    : 70;

            // 🌧 weather type
            List<Map<String, Object>> weatherList =
                    (List<Map<String, Object>>) response.get("weather");

            String weatherMain = weatherList != null && !weatherList.isEmpty()
                    ? weatherList.get(0).get("main").toString()
                    : "Clouds";

            int weatherCode = mapWeather(weatherMain);

            return new WeatherData(temperature, humidity, weatherCode);

        } catch (Exception e) {
            log.warn("Weather API failed: {}", e.getMessage());
            return getDefaultWeather();
        }
    }

    // 🔥 DTO CLASS (INNER CLASS)
    public record WeatherData(double temperature, double humidity, int weatherCode) {}
}