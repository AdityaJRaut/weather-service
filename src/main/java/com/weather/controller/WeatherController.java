package com.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.model.Weather;
import com.weather.service.WeatherService;

@RestController
@RequestMapping("v1/weather")
public class WeatherController {

	@Autowired
	WeatherService service;

	@GetMapping
	public ResponseEntity<Weather> getWeather(@RequestParam(defaultValue = "melbourne") String city) {
		System.out.println(city);
		Weather weather = service.getWeather(city);
		return ResponseEntity.ok(weather);
	}
}
