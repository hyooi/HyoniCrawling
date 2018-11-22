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
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyoni.crawling.vo.NaverAuthVO;

@Repository("SearchWordDao")
public class SearchWordDao {
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
	private final String CLIENT_ID = "0f5PGUtgYLnO09cq5LpN";
	private final String CLEINT_SECRET = "XvTpNcCebC";
	
	public HashMap<String, Object> getSearchWordFromBlog(String searchWord) throws Exception {
		String searchedData = null;
		HashMap<String, Object> resultData = new HashMap<>();	
		
		/*검색API 호출*/
		try {
			String text = URLEncoder.encode(searchWord, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text; // json 결과
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
			con.setRequestProperty("X-Naver-Client-Secret", CLEINT_SECRET);
			
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
		String searchedData = null;
		
		/*검색API 호출*/
		try {
			String text = URLEncoder.encode(searchWord, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/search/webkr.json?query=" + text; // json 결과
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
			con.setRequestProperty("X-Naver-Client-Secret", CLEINT_SECRET);
			
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
					.userAgent(USER_AGENT).get();
			items = doc.select("._related_keyword_ul").select("li");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(items.text().isEmpty())	relatedSearches = "\r없음";
		else for (Element item : items)	relatedSearches += "\r└ "+item.text();
		
		return relatedSearches;
	}
	
	public HashMap<String, Object> getAmountSearches(String searchWord) throws Exception {
		String connectUrl = "https://manage.searchad.naver.com/keywordstool?format=json&siteId=&mobileSiteId=&hintKeywords=";
		String connectUrl2 = "&includeHintKeywords=0&showDetail=1&biztpId=&mobileBiztpId=&month=&event=&keyword=";
		NaverAuthVO naverAuth = getLoginToNaverAdSystem();
		
		Document doc = null;
		String item = null;
		try {
			doc = Jsoup.connect(connectUrl+URLEncoder.encode(searchWord, "UTF-8")+connectUrl2)
					.header("Accept", "application/json, text/plain, */*")
					.header("Accept-encoding", "gzip, deflate, br")
					.header("Accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
					.header("authorization", "Bearer "+naverAuth.getToken())
					.header("cookie", "NNB="+naverAuth.getNnb()+"; npic=r8a+J0JV4FaSmBht0wpBrEOt4KewcPWSpth6qRRg0nJ7LsmcLCsdOt/IOTadVdcCCA==; PCID=15007847098034900797112; _ga=GA1.2.78581027.1500784703; ASID=73a104bf0000015d918bd3200000004b; nx_ssl=2; BMR=s=1541822086278&r=https%3A%2F%2Fm.blog.naver.com%2Foccidere%2F220851125347&r2=https%3A%2F%2Fwww.google.co.kr%2F; page_uid=T+ijowpVuEVsstSLyHGssssst0K-190454; nid_inf=1941093750; NID_AUT=NZf4NQ0kJnxEXLL/0YqpEdfqeiDeLuIDsGgxGH32YMQ4JhyXjxVhwG1o24aHU+Z8; NID_JKL=ifoeNwXBJxta1GWaUA8bCwQdJ+EN6mKZZ4l1oIe8iqQ=; NID_SES=AAABmlD16JnUkiY+n8hZMVrfykoVXAMFvwOVvnNuWeF0rQUmviKhEKRxRJzM6/FFLOMDdW8gDP9TjyBbMwgeAiDxv8RiTYzmxm1ZLmteJTfCPONcUnwubsLc+1Hqe9AgoOUr+bb/mIU/DQmgtIosgwwPgKFjcQdAXHylHwx+ENm86W8w9n5fLXWuFv6+vCbltXX4tOv9izf5UUH5Mn6jQh7GRE6yvgG+J0UHHYB6LtrcizEf6vv6s+C6IthWgvTLsj1u8vJEyCJlbzUEJWB8E1cg55c2Ho3aQnSYn0L3wfD5veGale/4hJxXztfd3iKwqO+l8aQs2DXfqPXJL/nJTH0TWwjKhymU7LjciPXDt5epbgTbdB/yVIiaGBmpY6uAVME7wQWiwZFvmiaFm3DKhwYijEj4JKbBTrmwSbIXdcyWSgIYNFBxdUeBPBs0sstxrR8xYdJyn/siGqs10v/nUftXJkIaepo4K5Aa1JFoBW5SIK0EXYEVheVRxFMr6THgC9bmMo3fq6JWh98T0BymypXmo+GH7xhuxewoSn9OKyNK5Psf; 066c80626d06ffa5b32035f35cabe88d=%3ER%95%04%E3%D26%F0%3B%94%87%25%BB%CA%FF%BDzc%C5%F06%E0%06WZ%3E%91%09z%B8%D8%F3%14%D6t%D5%98%251%1B%F1%E0%EA%E1%11%D1%DF%C1q%EF%C8%17%E3%B3%9B%8D%9E%C6%D8%DE%FA%B0m%81FGu%2B%12%2C8%F7%91%92%AC%14%F0%A7M%0C%A6%82%1B%ED%9D%5B6%FF%40x%EA%9D%9E%F7w1%0D%27%27%40%7EI%A3%0E%8B%E9%DF%2AF%0Cm%D0%3C.%BA%F1%07_lcn%DBDE%AF%F1%1E%01%F0%DFO%A5%21%17%9E%07%7D%AC%87%CB%1D%8E%3F%B3; 1a5b69166387515780349607c54875af=%28%DE%DD%D0%B4%DE%87%09; 1b25d439ceac51f5d414df7689017f6c=%AE%E2%BFK%3D%9B%07K%FD%15%0B%15%DC%97%95%C4%A3%B8%DD%E9%D6%3F%12a%3B%D6%CDj%DA%11%DCB%BB%2B%19%3B%C37%C1%CA%14%7D%7C%11%B9%15%25%83%EBOJ%1B%A8e%19%F9%AF%D2k%1C%BB%EA%8E%C32%A1%22U%D8L%9A%1F%D87%26%18%CF%D4%C2%B6%1B%AB%3A%F9%EDJ%88%F6j%B3%22%8E+%84%93%7E%85%5EC%BE%3E%90%89%BC%5Du%ECxy%25d%8F%AF%1E%0Et%91%26%D4%15%AEN%E4%EB%195%0B%03%98%7F%CFS.%09%27%8A%FF%D6lL%3Bo+%18%B1%B5Vl%404t%FF%BCH%04%F5%A7%D8%26%24%FB%3A%94%BE%B5%D8%AB%EDg%1D%3A%0D%F9%FD%C6F%D1%DA%AF%B6h%FBCFx4%0E%E5%FE%08%DF%F5%F6%AA2%F9K-%CE%92%5Du%ECxy%25d%8F%B8%3A%DB%21%7F%11%B8V%D0%FB8%40%BA%16%E1%D9%D4NS%DB%EB%0F%A2Dr%8CM%03%C2%D1%A4%0A%C2%14%B8%BD%09%A4A%EA%A1h2%08%E1%0FX%B8%3B%866%991K%F9%9D%06%DE%0EV%06S%F9%85%EE%A9%AC%D7%F6%B3%A2%E8%81%5CY%DB%8C%FBD%7C%B0Zo%A1%C0%BE%29%8Ceg%40Y%BC%A1%E9%BE%E0%1C%14%25%BB%B4%90%CF%93%14%97%0C%AB%A1P%07%E3%EFn%F6ZY%D6%BD%CAd%A5%B1C%14%F4%0E%1B.%7B%00%D6%7FX%22F%E40%87%EF-%AA%9D%BE%E4%83%26%179%EA%10%D1%DA%AF%B6h%FBCF%DB%F2%FDc%19%C8%AC9%DAmw%89%D5+%5C%3D%0D%B8%1B%87%27.%F7%E7%065%3B%25h%17%C5%3D%80%06%CB%EDz%84T%DA%1A-%0F%CC%84%9Ep%BA%FE%CC%05b%0Fs%D4%1A%A6%99%B3b%19P%B2%8ENd%22%DDpAH%DE%14%23%D7%F4%07%AB%8E%3E%F8%BE%06%CD%93E%B6%F2%DAJ%06%BE%97%EE%B5%8C%88%F2L%89XY%25Au%0Ff%D5%28%D4%94%CAr%C1%09m%D63f%F8")
					.header("referer", "https://manage.searchad.naver.com/customers/1506634/tool/keyword-planner")
					.header("x-accept-language", "ko-KR")
					.userAgent(USER_AGENT).ignoreContentType(true).get();
			item = doc.select("body").text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*JSON TO MAP 컨버팅*/
		JSONObject obj = 	new JSONObject(item);
		JSONArray jsonArr = obj.getJSONArray("keywordList");
		HashMap<String, Object> map;	
		ObjectMapper mapper = new ObjectMapper();
		map = mapper.readValue(jsonArr.get(0).toString(), new TypeReference<HashMap<String, Object>>(){});
		
		return map;
	}
	
	@Cacheable(cacheNames="naverLoginCookie")
	private NaverAuthVO getLoginToNaverAdSystem() throws Exception {
		/*STEP1.NNB쿠키GET*/
		String cookieUrl = "https://lcs.naver.com/m?u=https%3A%2F%2Fsearchad.naver.com%2Flogin&e=&os=Win32&ln=ko-KR&sr=1920x1080&pr=1&bw=506&bh=952&c=24&j=N&k=Y&i=&ct=&navigationStart=1541856641040&unloadEventStart=1541856641062&unloadEventEnd=1541856641062&fetchStart=1541856641043&domainLookupStart=1541856641043&domainLookupEnd=1541856641043&connectStart=1541856641043&connectEnd=1541856641043&requestStart=1541856641045&responseStart=1541856641058&responseEnd=1541856641061&domLoading=1541856641069&domInteractive=1541856641376&domContentLoadedEventStart=1541856641376&domContentLoadedEventEnd=1541856641376&ngt=1&pid=9196f349a374ca5d550c4d3d68e535da&ts=1541856641499&EOU";
		Response cookieNNBResponse = Jsoup.connect(cookieUrl)
                .header("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Connection", "keep-alive")
                .header("Host", "lcs.naver.com")
                .referrer("https://searchad.naver.com/login")
                .userAgent(USER_AGENT)
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute()
                ;
		
		/*STEP2 네이버 로그인 처리 */
		cookieUrl = "https://searchad.naver.com/auth/login";
		JSONObject loginData = new JSONObject();
		loginData.put("loginId", "hyoin123");
		loginData.put("loginPwd", "service!nav");
		
		String loginToken = Jsoup.connect(cookieUrl)
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("content-length", "44")
                .header("content-Type", "application/json")
                .cookie("NNB", cookieNNBResponse.cookie("NNB"))
                .header("origin", "https://searchad.naver.com")
                .header("Referer", "https://searchad.naver.com/login")
                .requestBody(loginData.toString())
                .userAgent(USER_AGENT)
                .ignoreContentType(true)
                .post().body().text()
                ;
		
		loginData = new JSONObject(loginToken);
		NaverAuthVO naverAuthVO = new NaverAuthVO(cookieNNBResponse.cookie("NNB"), (String)loginData.get("token"));
		
		return naverAuthVO;
	}
}