package com.hyoni.crawling.chatbot.service;

import java.util.HashMap;

public interface WeatherService {
	String getWeathers();
	String getCertainWeather(String region) throws Exception;
	String getConvertCondition(HashMap<String, Object> weather);
}
