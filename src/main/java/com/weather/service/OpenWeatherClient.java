package com.weather.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.weather.model.Weather;

@Component
public class OpenWeatherClient {

	@Value("${openweather.api.key}")
	private String apiKey;

	@Autowired
	private RestTemplate restTemplate;

	
	@SuppressWarnings("unchecked")
	public Weather getWeather(String city) {
		Map<String,Object> response = restTemplate.getForObject(
				"http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey,
				Map.class);
		System.out.println(response);
		Map<String, Object> wind = (Map<String, Object>) response.get("wind");
	    Map<String, Object> main = (Map<String, Object>) response.get("main");
	    System.out.println(wind+" "+main);
	    double kelvinTemp = ((Number) main.get("temp")).doubleValue();
	    int temperatureCelsius = (int) (kelvinTemp - 273.15);

	    double windSpeedRaw = ((Number) wind.get("speed")).doubleValue();
	    int windSpeed = (int) Math.round(windSpeedRaw);

	    return new Weather(windSpeed, temperatureCelsius);
//		return null;
	}
}
