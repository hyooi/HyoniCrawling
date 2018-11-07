package com.hyoni.crawling.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyoni.crawling.dao.WeatherDao;
import com.hyoni.crawling.service.WeatherService;

@Service
public class WeatherServiceImpl implements WeatherService {	
	@Autowired
	WeatherDao weatherDao;

	@Override
	public String getWeathers() {
		String weathers = "";
		try {
			weathers += "\r서울 : "+getConvertCondition(weatherDao.getCertainWeather("seoul"));
			weathers += "\r인천 : "+getConvertCondition(weatherDao.getCertainWeather("incheon"));
			weathers += "\r춘천 : "+getConvertCondition(weatherDao.getCertainWeather("chuncheon"));
			weathers += "\r강릉 : "+getConvertCondition(weatherDao.getCertainWeather("gangrung"));
			weathers += "\r청주 : "+getConvertCondition(weatherDao.getCertainWeather("chungju"));
			weathers += "\r대전 : "+getConvertCondition(weatherDao.getCertainWeather("daejeon"));
			weathers += "\r전주 : "+getConvertCondition(weatherDao.getCertainWeather("jeonju"));
			weathers += "\r광주 : "+getConvertCondition(weatherDao.getCertainWeather("gwangju"));
			weathers += "\r대구 : "+getConvertCondition(weatherDao.getCertainWeather("daegu"));
			weathers += "\r울산 : "+getConvertCondition(weatherDao.getCertainWeather("ulsan"));
			weathers += "\r부산 : "+getConvertCondition(weatherDao.getCertainWeather("busan"));
			weathers += "\r제주 : "+getConvertCondition(weatherDao.getCertainWeather("jeju"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weathers;
	}

	@Override
	public String getCertainWeather(String region) throws Exception {
		HashMap<String, Object> weather = weatherDao.getCertainWeather(region);
		return getConvertCondition(weather);
	}
	
	@Override
	public String getConvertCondition(HashMap<String, Object> weather) {
		String weatherCondition = "";
		double temperature = Double.parseDouble((String) weather.get("T1H"));
		int skyCondition = (int) weather.get("SKY");
		int rainCondition = (int) weather.get("PTY");
		
		/*
		 * 하늘상태(SKY) : 맑음(1), 구름조금(2), 구름많음(3), 흐림(4)
		 * 강수형태(PTY) : 없음(0), 비(1), 진눈깨비(2), 눈(3)
		 * */
		if (rainCondition == 0) {
			if(skyCondition == 1)	weatherCondition = "맑음";
			else if(skyCondition == 2) weatherCondition = "구름조금";
			else if(skyCondition == 3) weatherCondition = "구름많음";
			else if(skyCondition == 4) weatherCondition = "흐림";
		}else {
			if(rainCondition == 1)	weatherCondition = "비";
			else if(rainCondition == 2) weatherCondition = "진눈깨비";
			else if(rainCondition == 3) weatherCondition = "눈";
		}
		return temperature+"℃, "+weatherCondition;
	}
}
