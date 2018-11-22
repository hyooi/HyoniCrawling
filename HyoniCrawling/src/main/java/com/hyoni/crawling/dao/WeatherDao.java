package com.hyoni.crawling.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("WeatherDao")
public class WeatherDao {
	public HashMap<String, Integer> getRegionKey(String region){
		HashMap<String, Integer> regionKey = new HashMap<>();
		/*
		 * DB연동 후 DB에서 가져와서 사용할 것
		 * 서울 : 60, 127
		 * 인천 : 55, 124
		 * 춘천 : 73, 134
		 * 강릉 : 92, 131ㅒ
		 * 청주 : 69, 107
		 * 대전 : 67, 100
		 * 전주 : 63, 89
		 * 광주 : 58, 74
		 * 대구 : 89, 90
		 * 울산 : 102, 84
		 * 부산 : 98, 76
		 * 제주 : 52, 38
		 * */

		if(region.equals("seoul")) {
			regionKey.put(region+"X", 60);
			regionKey.put(region+"Y", 127);
		}else if(region.equals("incheon")) {
			regionKey.put(region+"X", 55);
			regionKey.put(region+"Y", 124);
		}else if(region.equals("chuncheon")) {
			regionKey.put(region+"X", 73);
			regionKey.put(region+"Y", 134);
		}else if(region.equals("gangrung")) {
			regionKey.put(region+"X", 92);
			regionKey.put(region+"Y", 131);
		}else if(region.equals("chungju")) {
			regionKey.put(region+"X", 69);
			regionKey.put(region+"Y", 107);
		}else if(region.equals("daejeon")) {
			regionKey.put(region+"X", 67);
			regionKey.put(region+"Y", 100);
		}else if(region.equals("jeonju")) {
			regionKey.put(region+"X", 63);
			regionKey.put(region+"Y", 89);
		}else if(region.equals("gwangju")) {
			regionKey.put(region+"X", 58);
			regionKey.put(region+"Y", 74);
		}else if(region.equals("daegu")) {
			regionKey.put(region+"X", 89);
			regionKey.put(region+"Y", 90);
		}else if(region.equals("ulsan")) {
			regionKey.put(region+"X", 102);
			regionKey.put(region+"Y", 84);
		}else if(region.equals("busan")) {
			regionKey.put(region+"X", 98);
			regionKey.put(region+"Y", 76);
		}else if(region.equals("jeju")) {
			regionKey.put(region+"X", 52);
			regionKey.put(region+"Y", 38);
		}
		
		return regionKey;
	}
	
	public HashMap<String, Object> getCertainWeather(String region) throws Exception {
		HashMap<String, Integer> regionKey = getRegionKey(region);
		HashMap<String, Object> certainWeather = new HashMap<>();
		final String END_POINT = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2";
		final String REQ_URL = "/ForecastTimeData?ServiceKey=";
		final String SERVICE_KEY = "0REW6hqi1itJS92HXKAd3NyWcubyfcjkjT%2B7dUjb9IET8aaDGrOSwoPIjqmNGE3FkITP7pwAoHBiH2u%2F3p2F8A%3D%3D";
		
		/*현재 시간으로부터 30분 전으로 API호출*/
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.MINUTE, -30);
		
		String base_date = new SimpleDateFormat("YYYYMMdd").format(cal.getTime());
		String base_time = new SimpleDateFormat("HHMM").format(cal.getTime());

		int pageNo = 1;
		int numofRows = 40;
		int nX = regionKey.get(region+"X");
		int nY = regionKey.get(region+"Y");
		String weatherData = null;
	
		/*날씨API 호출*/
		try {
			URL url = new URL(END_POINT+REQ_URL+SERVICE_KEY+"&base_date="+base_date+"&base_time="+base_time+"&nx="+nX+"&ny="+nY+"&pageNo="+pageNo+"&numOfRows="+numofRows+"&_type=json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader  rd;
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)	rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			else rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			weatherData = rd.readLine();
			rd.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*JSON TO MAP 컨버팅*/
		JSONObject obj = 	new JSONObject(weatherData);
		JSONArray jsonArr = obj.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
		HashMap<String, Object> map;
		ObjectMapper mapper = new ObjectMapper();
		
		if (jsonArr != null) {
			int jsonSize = jsonArr.length();
			for(int i = 0; i < jsonSize; i++) {
				map =mapper.readValue(jsonArr.get(i).toString(), new TypeReference<HashMap<String, Object>>(){});
				if (map.get("category").equals("T1H"))	certainWeather.put("T1H", map.get("fcstValue").toString())	;	//기온
				else if (map.get("category").equals("SKY"))	certainWeather.put("SKY", map.get("fcstValue"));	//하늘상태
				else if (map.get("category").equals("PTY"))	certainWeather.put("PTY", map.get("fcstValue"));	//강수형태
			}
		}
		return certainWeather;
	}	
}