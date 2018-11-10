package com.hyoni.crawling.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
            while ((inputLine = br.readLine()) != null)	response.append(inputLine);
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
            while ((inputLine = br.readLine()) != null)	response.append(inputLine);
            br.close();
            
            searchedData = response.toString();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		/*JSON TO MAP 컨버팅*/
		JSONObject obj = 	new JSONObject(searchedData);
		return obj.getInt("total");
	}
	
	public String getRelatedSearches(String searchWord) {
		String connectUrl = "https://search.naver.com/search.naver?where=nexearch&sm=tab_jum&query=";
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
		String relatedSearches = "";
		
		Document doc = null;
		Elements items = null;
		try {
			doc = Jsoup.connect(connectUrl+URLEncoder.encode(searchWord, "UTF-8"))
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
					.header("Accept-encoding", "gzip, deflate, br")
					.header("Accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
					.header("Cache-control", "max-age=0")
					.header("Cookie", "NNB=6OFX6PLLOZLFS; npic=r8a+J0JV4FaSmBht0wpBrEOt4KewcPWSpth6qRRg0nJ7LsmcLCsdOt/IOTadVdcCCA==; PCID=15007847098034900797112; _ga=GA1.2.78581027.1500784703; ASID=73a104bf0000015d918bd3200000004b; nx_open_so=1; nx_ssl=2; BMR=s=1541822086278&r=https%3A%2F%2Fm.blog.naver.com%2Foccidere%2F220851125347&r2=https%3A%2F%2Fwww.google.co.kr%2F; page_uid=T+ijEdpVuE8ssvWMiKRssssstvd-227893; _naver_usersession_=smXnTb4LYU8T4vnUweZA2w==")
					.header("Upgrade-insecure-requests", "1")
					.userAgent(userAgent).get();
			items = doc.select("._related_keyword_ul").select("li");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(items.text().isEmpty())	relatedSearches = "\r없음";
		else for (Element item : items)	relatedSearches += "\r└ "+item.text();
		
		return relatedSearches;
	}
}