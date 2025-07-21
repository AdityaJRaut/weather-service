package com.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.weather.model.Weather;
import com.weather.response.WeatherApiResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class WeatherStackClient {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${weatherstack.api.key}")
	private String weatherStackKey;
	@Autowired
	private OpenWeatherClient openWeatherClient;

	@CircuitBreaker(name = "weatherstack", fallbackMethod = "fallbackToOpenWeather")
	public Weather getFromWeatherStack(String city) {
		String url = "http://api.weatherstack.com/current?access_key=" + weatherStackKey + "&query=" + city;
		WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

		if (response == null) {
			throw new RuntimeException("Invalid response from WeatherStack");
		}

		int windSpeed = (int) response.getCurrent().get("wind_speed");
		int temp = (int) response.getCurrent().get("temperature");

		return new Weather(windSpeed, temp);
	}

	public Weather fallbackToOpenWeather(String city, Throwable throwable) {
		return openWeatherClient.getWeather(city);
	}
}
