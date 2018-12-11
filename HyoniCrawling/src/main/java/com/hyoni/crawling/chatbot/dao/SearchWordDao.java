package com.hyoni.crawling.chatbot.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyoni.crawling.common.dao.NaverAdsystemDao;

@Repository("SearchWordDao")
public class SearchWordDao {
	private static final Logger logger = LoggerFactory.getLogger(SearchWordDao.class);
	
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
	private final String CLIENT_ID = "0f5PGUtgYLnO09cq5LpN";
	private final String CLEINT_SECRET = "XvTpNcCebC";
	
	@Test
	public void getTopRandomKeyword() throws Exception {
		int[] month = {1,2,3,4,5,6,7,8,9,10,11,12};
		Document doc = null;
		String item = null;
		for (int idx1 : month) {
			String connectUrl = "https://manage.searchad.naver.com/keywordstool?format=json&siteId=&mobileSiteId=&hintKeywords=&includeHintKeywords=0&showDetail=1&biztpId=&mobileBiztpId=&month="+idx1+"&event=&keyword=";
			try {
				doc = Jsoup.connect(connectUrl)
						.header("Accept", "application/json, text/plain, */*")
						.header("Accept-encoding", "gzip, deflate, br")
						.header("Accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
						.header("authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjoiZ3lkbHMyMDAwOm5hdmVyIiwicm9sZSI6MCwiY2xpZW50SWQiOiJuYXZlci1jb29raWUiLCJpc0FwaSI6ZmFsc2UsInVzZXJJZCI6MTcyNTUwNCwidXNlcktleSI6ImQxZTFiMjQ3LWMwNDUtNGRkMi04NDg5LWM0ZTdiZWI0OTRkMiIsImNsaWVudEN1c3RvbWVySWQiOjE1MDY2MzQsImlzc3VlVHlwZSI6InVzZXIiLCJuYmYiOjE1NDQwMjExODEsImlkcCI6InVzZXItZXh0LWF1dGgiLCJjdXN0b21lcklkIjoxNTA2NjM0LCJleHAiOjE1NDQwMjE4NDEsImlhdCI6MTU0NDAyMTI0MSwianRpIjoiZjE5NTUxZGItNTM2Ni00ZTc0LTg0NmQtNGFjZDFjMjgyMGIwIn0.QVDCt-2TZu81RAaJNuTLtvQjlaSTyXwW3FYVgbflllk")
						.header("cooike", "NNB=KPQY2DGKZYDVY; npic=nu7sG/qbJWMUTuvYwoiOmiiKLDdgMPIo7jb3dDb/3LG7tiJQRIaIcKkd3Vrb+jJICA==; nid_inf=1982949732; NID_AUT=yA28vl/OZCuzz8psAgvGxkTMoTe8MKrifFdPELIWOh2/Xj57Xh2Lso1tVoW2edtZ; NID_JKL=kK9crn9KiMIez8eb+Nms9oLx2DYXftcs4pADxan9jxs=; NID_SES=AAABmBexj0i0bRyYayzmiecSGHydJ7OeVtiMCm7pv3wDb4D0m7izdaIGx8ekx0DQgGk8jHry2dXy0VthdwxNSYbShHevinaQNsjdxIHkmJhnaR9wIpgpfWys2pdNqK8pqsK3D5Y9qVbkSwBY9+XRVgz3UbLiaF2t+udJvro1mApUkUZAzHO/eQKvoo141nsBnIp+T0AcJg7IL7WkXZ5PvtPlDeXTLoD/U6hZnC4/z4gIB/tgo+6c8aeE8h/yuOSjxmfvsy0SYBxAjoNHvArbEk5AE3pZxpFl9bw2Sddwx1TKB5gVxxRS2YLuWSAEg/4gQqfXxLiJiQRjnKy770qEqrw7Vy5b5Vp/uXQkOyo+T8Z6oXGFZ5r+xirp5tDNQpyU2yg7wQktuxASZyAZovOU4g5PE6u+B9uj0Z5Lu3fAhucHnQK/7s3ONGgRzx4JbaoU7ieYGSe18krYsfMorkNRf+ivy4rBlONJGTY8JcvuKrj5yZX1GZV3vqCcvXvnQBUTBXTirkERXQvZRiZudeeRye45Ad3CVRFhNQt80zc0ZIZELXm8; 066c80626d06ffa5b32035f35cabe88d=%3ER%95%04%E3%D26%F0%3B%94%87%25%BB%CA%FF%BDzc%C5%F06%E0%06W%01%08%7D%24%7Ed%F0%8BQ%AEQX%F1u%F2%877%AC%B5%92%F9%CB%AD%18%FCn%93%86%27S%7D%5C%EE%C8i%00%08%A7%DB%3AFGu%2B%12%2C8%F7%91%92%AC%14%F0%A7M%0C%A6%82%1B%ED%9D%5B6%FF%B5%85%F25n%EA%B0%1D%A0%1F%A7O%7D%8E%8EuVa%B7u%96%E6%B8%FA%3C.%BA%F1%07_lcn%DBDE%AF%F1%1E%019%D0%83%E5%92%E6%2B%F4%97%F3%2A%A8j%9CQ%B3; 1a5b69166387515780349607c54875af=%28%DE%DD%D0%B4%DE%87%09; 1b25d439ceac51f5d414df7689017f6c=%AE%E2%BFK%3D%9B%07K%FD%15%0B%15%DC%97%95%C4%A3%B8%DD%E9%D6%3F%12a%3B%D6%CDj%DA%11%DCB%BB%2B%19%3B%C37%C1%CA%14%7D%7C%11%B9%15%25%83%13%EF%83%24%C1%CB%5C%3E%CC%B2Z+%29%09%0AJsHr%C6Xj6%94TRy%DEC%3E%1F%D0%C8%7F%19%C2%B0%CB%FDaN5%27F%B31%3EC%29%2A%5C%94%FAE.%E0%97%7E%3E%EC%D1%9A%A8%D6%5B%BF1%B5%DB%07%A5%BA%85kO%AB%C3%06%F1%ABcj%9C%90%EC%F0%96%1Dh%DBi%82%B0%B5%CFu%9D%1B%F3%91Z%C1%BB%D6%03q%A4%8B%8Bi%5B%A0y%18%FC%F9%B6%FE%B4%99z%A4%8C%CE%A0sV%EC%A6u%1Aj-T%B9%DEYQ%F12%F7%FE5%D4y%83%D25%9D%E8%C0%DDq%1D%E6%AF%3D0%98%19%08%E3%91%1B%10%D5%E3%AD%CC%D2%DC%B6%E6Mx9A%02%FFQ%3Aa%14%1Ewu%9D%F0%1E%C3%40%2A%F8%14tz%5B%F2J%EB8B%2C%22%D6%DE%C7%7E%9D%C1%9A%FA%85%3E%FE%B74%14%92%A9L%F8%AF%7F%0A3%5CZ%D3%C3%EE%DFI%22%27%119%CA7%12%A6%0E%827%B2%A6%87%2BE%85%5C%02%C6v%3A%A6B%0B%C0d%B0%C0e%F4%22%CA%F0%F0%0E%3Fq%1C%E4%D6%86%84k%C7%0Bf%B1u8%FB%D6%1E%0E%D6Df%B7%C9D%5Ct%CA%23%92I%B5%23g%FA%7B%AEN%E4%EB%195%0B%03%98%7F%CFS.%09%27%8A%FF%D6lL%3Bo+%18%B1%B5Vl%404t%FF%BCH%04%F5%A7%D8%26%244%13%0A%C8%5E%DF%0C%EES%0CqnZ%B6%86%92%EA%17%C4%A81%F7g%D7Qm_%CA%FD%DE%3A%06%81%5CY%DB%8C%FBD%7C%F4j%14%AC%BF%2FNC%93I%C2%7D%FFs%B4J%96%D1%7F%3F%BD%7D%A2%E5%FET%192%F8%25%96%A2%C0%B8%21%8A%03%06%E2%D7%60%3C%D7%88%DF%F6N%21%8B%E0%91W%F9%BBn%3C%00%B0%E5%E2%95%F7%B8%0D%FD%805%C1GoW%93%7C%8A%3C%E2%85I%F19%0A%87%A6%40%0D%7E%28%85T%E2H%D7%5B5%E7%9A3%EF%B3%C8%3A%E8j%D8%86%F6n%EB%17%9F%A2%28%90%EA%19+%EB8%A0%03%2A%CBY%CAN%2C%1D%8C%FAJ1s%C8%A7%04%01%08%14c%5C%B8%8E%FD%87%AD%A3%FC%9B%00C%ED%7B%9B%91N%B2q%049c%FD%28%8F.f%D4%D6%BE")
//						.header("authorization", "Bearer "+NaverAdsystemDao.naverAuthVO.getToken())
//						.header("cookie", "NNB="+NaverAdsystemDao.naverAuthVO.getNnb()+"; "+"npic=nu7sG/qbJWMUTuvYwoiOmiiKLDdgMPIo7jb3dDb/3LG7tiJQRIaIcKkd3Vrb+jJICA==; nid_inf=1982949732; NID_AUT=yA28vl/OZCuzz8psAgvGxkTMoTe8MKrifFdPELIWOh2/Xj57Xh2Lso1tVoW2edtZ; NID_JKL=kK9crn9KiMIez8eb+Nms9oLx2DYXftcs4pADxan9jxs=; NID_SES=AAABmBexj0i0bRyYayzmiecSGHydJ7OeVtiMCm7pv3wDb4D0m7izdaIGx8ekx0DQgGk8jHry2dXy0VthdwxNSYbShHevinaQNsjdxIHkmJhnaR9wIpgpfWys2pdNqK8pqsK3D5Y9qVbkSwBY9+XRVgz3UbLiaF2t+udJvro1mApUkUZAzHO/eQKvoo141nsBnIp+T0AcJg7IL7WkXZ5PvtPlDeXTLoD/U6hZnC4/z4gIB/tgo+6c8aeE8h/yuOSjxmfvsy0SYBxAjoNHvArbEk5AE3pZxpFl9bw2Sddwx1TKB5gVxxRS2YLuWSAEg/4gQqfXxLiJiQRjnKy770qEqrw7Vy5b5Vp/uXQkOyo+T8Z6oXGFZ5r+xirp5tDNQpyU2yg7wQktuxASZyAZovOU4g5PE6u+B9uj0Z5Lu3fAhucHnQK/7s3ONGgRzx4JbaoU7ieYGSe18krYsfMorkNRf+ivy4rBlONJGTY8JcvuKrj5yZX1GZV3vqCcvXvnQBUTBXTirkERXQvZRiZudeeRye45Ad3CVRFhNQt80zc0ZIZELXm8; 066c80626d06ffa5b32035f35cabe88d=%3ER%95%04%E3%D26%F0%3B%94%87%25%BB%CA%FF%BDzc%C5%F06%E0%06W%01%08%7D%24%7Ed%F0%8BQ%AEQX%F1u%F2%877%AC%B5%92%F9%CB%AD%18%FCn%93%86%27S%7D%5C%EE%C8i%00%08%A7%DB%3AFGu%2B%12%2C8%F7%91%92%AC%14%F0%A7M%0C%A6%82%1B%ED%9D%5B6%FF%B5%85%F25n%EA%B0%1D%A0%1F%A7O%7D%8E%8EuVa%B7u%96%E6%B8%FA%3C.%BA%F1%07_lcn%DBDE%AF%F1%1E%019%D0%83%E5%92%E6%2B%F4%97%F3%2A%A8j%9CQ%B3; 1a5b69166387515780349607c54875af=%28%DE%DD%D0%B4%DE%87%09; 1b25d439ceac51f5d414df7689017f6c=%AE%E2%BFK%3D%9B%07K%FD%15%0B%15%DC%97%95%C4%A3%B8%DD%E9%D6%3F%12a%3B%D6%CDj%DA%11%DCB%BB%2B%19%3B%C37%C1%CA%14%7D%7C%11%B9%15%25%83%13%EF%83%24%C1%CB%5C%3E%CC%B2Z+%29%09%0AJsHr%C6Xj6%94TRy%DEC%3E%1F%D0%C8%7F%19%C2%B0%CB%FDaN5%27F%B31%3EC%29%2A%5C%94%FAE.%E0%97%7E%3E%EC%D1%9A%A8%D6%5B%BF1%B5%DB%07%A5%BA%85kO%AB%C3%06%F1%ABcj%9C%90%EC%F0%96%1Dh%DBi%82%B0%B5%CFu%9D%1B%F3%91Z%C1%BB%D6%03q%A4%8B%8Bi%5B%A0y%18%FC%F9%B6%FE%B4%99z%A4%8C%CE%A0sV%EC%A6u%1Aj-T%B9%DEYQ%F12%F7%FE5%D4y%83%D25%9D%E8%C0%DDq%1D%E6%AF%3D0%98%19%08%E3%91%1B%10%D5%E3%AD%CC%D2%DC%B6%E6Mx9A%02%FFQ%3Aa%14%1Ewu%9D%F0%1E%C3%40%2A%F8%14tz%5B%F2J%EB8B%2C%22%D6%DE%C7%7E%9D%C1%9A%FA%85%3E%FE%B74%14%92%A9L%F8%AF%7F%0A3%5CZ%D3%C3%EE%DFI%22%27%119%CA7%12%A6%0E%827%B2%A6%87%2BE%85%5C%02%C6v%3A%A6B%0B%C0d%B0%C0e%F4%22%CA%F0%F0%0E%3Fq%1C%E4%D6%86%84k%C7%0Bf%B1u8%FB%D6%1E%0E%D6Df%B7%C9D%5Ct%CA%23%92I%B5%23g%FA%7B%AEN%E4%EB%195%0B%03%98%7F%CFS.%09%27%8A%FF%D6lL%3Bo+%18%B1%B5Vl%404t%FF%BCH%04%F5%A7%D8%26%244%13%0A%C8%5E%DF%0C%EES%0CqnZ%B6%86%92%EA%17%C4%A81%F7g%D7Qm_%CA%FD%DE%3A%06%81%5CY%DB%8C%FBD%7C%F4j%14%AC%BF%2FNC%93I%C2%7D%FFs%B4J%96%D1%7F%3F%BD%7D%A2%E5%FET%192%F8%25%96%A2%C0%B8%21%8A%03%06%E2%D7%60%3C%D7%88%DF%F6N%21%8B%E0%91W%F9%BBn%3C%00%B0%E5%E2%95%F7%B8%0D%FD%805%C1GoW%93%7C%8A%3C%E2%85I%F19%0A%87%A6%40%0D%7E%28%85T%E2H%D7%5B5%E7%9A3%EF%B3%C8%3A%E8j%D8%86%F6n%EB%17%9F%A2%28%90%EA%19+%EB8%A0%03%2A%CBY%CAN%2C%1D%8C%FAJ1s%C8%A7%04%01%08%14c%5C%B8%8E%FD%87%AD%A3%FC%9B%00C%ED%7B%9B%91N%B2q%049c%FD%28%8F.f%D4%D6%BE")
						.header("referer", "https://manage.searchad.naver.com/customers/1506634/tool/keyword-planner")
						.header("x-accept-language", "ko-KR")
						.userAgent(USER_AGENT).ignoreContentType(true).get();
				item = doc.text();
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*JSON TO MAP 컨버팅*/
			JSONObject obj = 	new JSONObject(item);
			JSONArray jsonArr = obj.getJSONArray("keywordList");
			List<HashMap<String, Object>> keywordList;	
			ObjectMapper mapper = new ObjectMapper();
			keywordList = mapper.readValue(jsonArr.toString(), new TypeReference<List<HashMap<String, Object>>>(){});

			int monthlyPcQcCnt;
			int monthlyMobileQcCnt;
			int resultBlogCnt;
			for (int idx2 = 0; idx2 < keywordList.size(); idx2++) {
				System.out.println(idx2+", "+keywordList.get(idx2).get("relKeyword"));
				monthlyPcQcCnt = keywordList.get(idx2).get("monthlyPcQcCnt").toString().startsWith("<")? 0: (int) keywordList.get(idx2).get("monthlyPcQcCnt");
				monthlyMobileQcCnt = keywordList.get(idx2).get("monthlyMobileQcCnt").toString().startsWith("<")? 0: (int) keywordList.get(idx2).get("monthlyMobileQcCnt");
				if(monthlyPcQcCnt+monthlyMobileQcCnt > 2000) {
					resultBlogCnt = (int) getSearchWordFromBlog(keywordList.get(idx2).get("relKeyword").toString()).get("blogTotal");
					if(resultBlogCnt < 300) System.out.println(idx1+"월 : "+resultBlogCnt+", 데이터 : "+keywordList.get(idx2).get("relKeyword"));
				}
			}
		}
	}
	
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
			resultData.put("blogLinks", linkRank.length() ==0? "없음" : linkRank.substring(0, linkRank.length()-2));
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
		
		Document doc = null;
		String item = null;
		try {
			doc = Jsoup.connect(connectUrl+URLEncoder.encode(searchWord, "UTF-8")+connectUrl2)
					.header("Accept", "application/json, text/plain, */*")
					.header("Accept-encoding", "gzip, deflate, br")
					.header("Accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
					.header("authorization", "Bearer "+NaverAdsystemDao.naverAuthVO.getToken())
					.header("cookie", "NNB="+NaverAdsystemDao.naverAuthVO.getNnb()+"; npic=r8a+J0JV4FaSmBht0wpBrEOt4KewcPWSpth6qRRg0nJ7LsmcLCsdOt/IOTadVdcCCA==; PCID=15007847098034900797112; _ga=GA1.2.78581027.1500784703; ASID=73a104bf0000015d918bd3200000004b; nx_ssl=2; BMR=s=1541822086278&r=https%3A%2F%2Fm.blog.naver.com%2Foccidere%2F220851125347&r2=https%3A%2F%2Fwww.google.co.kr%2F; page_uid=T+ijowpVuEVsstSLyHGssssst0K-190454; nid_inf=1941093750; NID_AUT=NZf4NQ0kJnxEXLL/0YqpEdfqeiDeLuIDsGgxGH32YMQ4JhyXjxVhwG1o24aHU+Z8; NID_JKL=ifoeNwXBJxta1GWaUA8bCwQdJ+EN6mKZZ4l1oIe8iqQ=; NID_SES=AAABmlD16JnUkiY+n8hZMVrfykoVXAMFvwOVvnNuWeF0rQUmviKhEKRxRJzM6/FFLOMDdW8gDP9TjyBbMwgeAiDxv8RiTYzmxm1ZLmteJTfCPONcUnwubsLc+1Hqe9AgoOUr+bb/mIU/DQmgtIosgwwPgKFjcQdAXHylHwx+ENm86W8w9n5fLXWuFv6+vCbltXX4tOv9izf5UUH5Mn6jQh7GRE6yvgG+J0UHHYB6LtrcizEf6vv6s+C6IthWgvTLsj1u8vJEyCJlbzUEJWB8E1cg55c2Ho3aQnSYn0L3wfD5veGale/4hJxXztfd3iKwqO+l8aQs2DXfqPXJL/nJTH0TWwjKhymU7LjciPXDt5epbgTbdB/yVIiaGBmpY6uAVME7wQWiwZFvmiaFm3DKhwYijEj4JKbBTrmwSbIXdcyWSgIYNFBxdUeBPBs0sstxrR8xYdJyn/siGqs10v/nUftXJkIaepo4K5Aa1JFoBW5SIK0EXYEVheVRxFMr6THgC9bmMo3fq6JWh98T0BymypXmo+GH7xhuxewoSn9OKyNK5Psf; 066c80626d06ffa5b32035f35cabe88d=%3ER%95%04%E3%D26%F0%3B%94%87%25%BB%CA%FF%BDzc%C5%F06%E0%06WZ%3E%91%09z%B8%D8%F3%14%D6t%D5%98%251%1B%F1%E0%EA%E1%11%D1%DF%C1q%EF%C8%17%E3%B3%9B%8D%9E%C6%D8%DE%FA%B0m%81FGu%2B%12%2C8%F7%91%92%AC%14%F0%A7M%0C%A6%82%1B%ED%9D%5B6%FF%40x%EA%9D%9E%F7w1%0D%27%27%40%7EI%A3%0E%8B%E9%DF%2AF%0Cm%D0%3C.%BA%F1%07_lcn%DBDE%AF%F1%1E%01%F0%DFO%A5%21%17%9E%07%7D%AC%87%CB%1D%8E%3F%B3; 1a5b69166387515780349607c54875af=%28%DE%DD%D0%B4%DE%87%09; 1b25d439ceac51f5d414df7689017f6c=%AE%E2%BFK%3D%9B%07K%FD%15%0B%15%DC%97%95%C4%A3%B8%DD%E9%D6%3F%12a%3B%D6%CDj%DA%11%DCB%BB%2B%19%3B%C37%C1%CA%14%7D%7C%11%B9%15%25%83%EBOJ%1B%A8e%19%F9%AF%D2k%1C%BB%EA%8E%C32%A1%22U%D8L%9A%1F%D87%26%18%CF%D4%C2%B6%1B%AB%3A%F9%EDJ%88%F6j%B3%22%8E+%84%93%7E%85%5EC%BE%3E%90%89%BC%5Du%ECxy%25d%8F%AF%1E%0Et%91%26%D4%15%AEN%E4%EB%195%0B%03%98%7F%CFS.%09%27%8A%FF%D6lL%3Bo+%18%B1%B5Vl%404t%FF%BCH%04%F5%A7%D8%26%24%FB%3A%94%BE%B5%D8%AB%EDg%1D%3A%0D%F9%FD%C6F%D1%DA%AF%B6h%FBCFx4%0E%E5%FE%08%DF%F5%F6%AA2%F9K-%CE%92%5Du%ECxy%25d%8F%B8%3A%DB%21%7F%11%B8V%D0%FB8%40%BA%16%E1%D9%D4NS%DB%EB%0F%A2Dr%8CM%03%C2%D1%A4%0A%C2%14%B8%BD%09%A4A%EA%A1h2%08%E1%0FX%B8%3B%866%991K%F9%9D%06%DE%0EV%06S%F9%85%EE%A9%AC%D7%F6%B3%A2%E8%81%5CY%DB%8C%FBD%7C%B0Zo%A1%C0%BE%29%8Ceg%40Y%BC%A1%E9%BE%E0%1C%14%25%BB%B4%90%CF%93%14%97%0C%AB%A1P%07%E3%EFn%F6ZY%D6%BD%CAd%A5%B1C%14%F4%0E%1B.%7B%00%D6%7FX%22F%E40%87%EF-%AA%9D%BE%E4%83%26%179%EA%10%D1%DA%AF%B6h%FBCF%DB%F2%FDc%19%C8%AC9%DAmw%89%D5+%5C%3D%0D%B8%1B%87%27.%F7%E7%065%3B%25h%17%C5%3D%80%06%CB%EDz%84T%DA%1A-%0F%CC%84%9Ep%BA%FE%CC%05b%0Fs%D4%1A%A6%99%B3b%19P%B2%8ENd%22%DDpAH%DE%14%23%D7%F4%07%AB%8E%3E%F8%BE%06%CD%93E%B6%F2%DAJ%06%BE%97%EE%B5%8C%88%F2L%89XY%25Au%0Ff%D5%28%D4%94%CAr%C1%09m%D63f%F8")
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
}