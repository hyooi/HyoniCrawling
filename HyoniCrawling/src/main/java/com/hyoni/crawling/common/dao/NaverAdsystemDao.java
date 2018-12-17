package com.hyoni.crawling.common.dao;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Repository;

import com.hyoni.crawling.common.vo.NaverAuthVO;

@Repository("NaverAdsystemDao")
public class NaverAdsystemDao {
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
	private NaverAuthVO naverAuthVO;
	
	public void getLoginToNaverAdSystem() throws Exception {
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
		naverAuthVO = new NaverAuthVO(cookieNNBResponse.cookie("NNB"), (String)loginData.get("token"));
	}
	
	public NaverAuthVO getNaverAuth() {
		return naverAuthVO;
	}
}
