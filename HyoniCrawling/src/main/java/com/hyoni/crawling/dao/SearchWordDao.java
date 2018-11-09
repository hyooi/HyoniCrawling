package com.hyoni.crawling.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("SearchWordDao")
public class SearchWordDao {
	public HashMap<String, Object> getSearchWordFromBlog(String searchWord) throws Exception {
		final String clientId = "0f5PGUtgYLnO09cq5LpN";// 애플리케이션 클라이언트 아이디값";
		final String clientSecret = "XvTpNcCebC";// 애플리케이션 클라이언트 시크릿값";
		String searchedData = null;
		HashMap<String, Object> resultData = new HashMap<>();	
		
		/*검색API 호출*/
		try {
			String text = URLEncoder.encode(searchWord, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text; // json 결과
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			else br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            searchedData = response.toString();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		/*JSON TO MAP 컨버팅*/
		JSONObject obj = 	new JSONObject(searchedData);
		JSONArray jsonArr = obj.getJSONArray("items");
		HashMap<String, Object> map;	
		ObjectMapper mapper = new ObjectMapper();
		
		resultData.put("blogTotal", obj.getInt("total"));
		
		if (jsonArr != null) {
			int jsonSize = jsonArr.length();
			String linkRank = "";
			String tmp;
			for(int i = 0; i < jsonSize; i++) {
				map =mapper.readValue(jsonArr.get(i).toString(), new TypeReference<HashMap<String, Object>>(){});
				tmp = (String) map.get("link");
				if(tmp.contains("tistory"))	linkRank += "t, ";
				else if(tmp.contains("naver"))	linkRank += "n, ";
				else	linkRank += "e, ";
			}
			resultData.put("blogLinks", linkRank.substring(0, linkRank.length()-2));
		}
		
		return resultData;
	}

	public int getWebDocsCnt(String searchWord) throws JSONException {
		final String clientId = "0f5PGUtgYLnO09cq5LpN";// 애플리케이션 클라이언트 아이디값";
		final String clientSecret = "XvTpNcCebC";// 애플리케이션 클라이언트 시크릿값";
		String searchedData = null;
		HashMap<String, Object> resultData = new HashMap<>();	
		
		/*검색API 호출*/
		try {
			String text = URLEncoder.encode(searchWord, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/search/webkr.json?query=" + text; // json 결과
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			else br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            searchedData = response.toString();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		/*JSON TO MAP 컨버팅*/
		JSONObject obj = 	new JSONObject(searchedData);
		return obj.getInt("total");
	}
	
	@Test
	public void getRelatedSearches() throws UnsupportedEncodingException {
		Document doc = null;
		String text = URLEncoder.encode("한국시리즈", "UTF-8");
		try {
			doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query="+text).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(doc);
	}

	public String getRelatedSearches(String searchWord) {
		return null;
	}
}