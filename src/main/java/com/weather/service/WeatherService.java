package com.weather.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weather.model.Weather;

@Service
public class WeatherService {

	@Autowired
	private OpenWeatherClient openWeatherClient;

	@Autowired
	private WeatherStackClient weatherStackClient;

	private Weather cachedWeather;
	private Instant lastFetched = Instant.MIN;

	public Weather getWeather(String city) {
		Instant now = Instant.now();
		boolean useCache = cachedWeather != null && now.minusSeconds(3).isBefore(lastFetched);

		if (useCache)
			return cachedWeather;

		try {
			System.out.println("In try");
			Weather weatherStack = weatherStackClient.getFromWeatherStack(city);
			cachedWeather = weatherStack;
			lastFetched = now;
			
			return weatherStack;
		} catch (Exception e) {
			try {
				System.out.println("in catch try");
				Weather fallback = openWeatherClient.getWeather(city);
				cachedWeather = fallback;
				lastFetched = now;
				return fallback;
			} catch (Exception e2) {
				if (cachedWeather != null)
					return cachedWeather;
				throw new RuntimeException("All providers failed.");
			}
		}
	}

}
