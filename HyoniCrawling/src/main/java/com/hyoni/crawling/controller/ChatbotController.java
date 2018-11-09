package com.hyoni.crawling.controller;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hyoni.crawling.service.SearchWordService;
import com.hyoni.crawling.service.WeatherService;
import com.hyoni.crawling.vo.ChatRequestVO;
import com.hyoni.crawling.vo.ChatResponseVO;
import com.hyoni.crawling.vo.KeyboardVO;
import com.hyoni.crawling.vo.MessageVO;

@RestController
public class ChatbotController {

	@Autowired
	private WeatherService weatherService;
	
	@Autowired
	private SearchWordService searchWordService;
	
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET)
	public KeyboardVO keyboard() {
		KeyboardVO keyboard = new KeyboardVO(new String[] {"안녕?", "날씨알려줘"});
		
		return keyboard;
	}
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public ChatResponseVO chatRequest(@RequestBody ChatRequestVO req) throws Exception {
		String reqContent = req.getContent();
		String resContent;
		
		if ((reqContent.contains("안녕"))||(reqContent.contains("하이")||(reqContent.contains("hi")))){
			resContent = "안녕하세요>_<";
		}else if(reqContent.startsWith("!날씨")) {
			if(reqContent.contains("서울"))	resContent = "현재 서울은 "+weatherService.getCertainWeather("seoul")+"입니다. :)";
			else if(reqContent.contains("인천"))	resContent = "현재 인천은 "+weatherService.getCertainWeather("incheon")+"입니다. :)";
			else if(reqContent.contains("춘천"))	resContent = "현재 춘천은 "+weatherService.getCertainWeather("chuncheon")+"입니다. :)";
			else if(reqContent.contains("강릉"))	resContent = "현재 강릉은 "+weatherService.getCertainWeather("gangrung")+"입니다. :)";
			else if(reqContent.contains("청주"))	resContent = "현재 청주는 "+weatherService.getCertainWeather("chungju")+"입니다. :)";
			else if(reqContent.contains("대전"))	resContent = "현재 대전은 "+weatherService.getCertainWeather("daejeon")+"입니다. :)";
			else if(reqContent.contains("전주"))	resContent = "현재 전주는 "+weatherService.getCertainWeather("jeonju")+"입니다. :)";
			else if(reqContent.contains("광주"))	resContent = "현재 광주는 "+weatherService.getCertainWeather("gwangju")+"입니다. :)";
			else if(reqContent.contains("대구"))	resContent = "현재 대구는 "+weatherService.getCertainWeather("daegu")+"입니다. :)";
			else if(reqContent.contains("울산"))	resContent = "현재 울산은 "+weatherService.getCertainWeather("ulsan")+"입니다. :)";
			else if(reqContent.contains("부산"))	resContent = "현재 부산은 "+weatherService.getCertainWeather("busan")+"입니다. :)";
			else if(reqContent.contains("제주"))	resContent = "현재 제주는 "+weatherService.getCertainWeather("jeju")+"입니다. :)";
			else	resContent = "전국 날씨를 알려드려요~"+weatherService.getWeathers();
		}else if(reqContent.startsWith("!검색")) {
			/*
			■ 키워드 : 겨울여행
			└ 피　씨 : 4,950 회
			└ 모바일 : 11,700 회
			└ 합　계 : 16,650 회
			└ 블로그 : 229,761 건
			└ 순　위 : n, n, n, n, n, n, n, n, n, n

			■ 연관키워드
			└ 제주도 겨울
			└ 국내 여행지 추천
			└ 12월 제주도
			└ 1박2일 국내여행
			└ 여수 겨울여행
			└ 제주도 12월 여행
			└ 국내 1박2일 겨울여행
			└ 제주도 겨울여행코스
			└ 겨울 제주
			└ 제주도 가을
			*/
			String[] searchWord = reqContent.split(" ");
			HashMap<String, Object> resultDataFromBlog = searchWordService.getSearchWordFromBlog(searchWord[1]);
			DecimalFormat commas = new DecimalFormat("#,###");
			
			resContent = "■ 키워드 : "+searchWord[1];
			resContent += "\r└ 피　씨 : ? 회";
			resContent += "\r└ 모바일 : ? 회";
			resContent += "\r└ 합　계 : "+commas.format(searchWordService.getWebDocsCnt(searchWord[1]))+" 회";
			resContent += "\r└ 블로그 : "+commas.format(resultDataFromBlog.get("blogTotal"))+" 건";
			resContent += "\r└ 순　위 : "+resultDataFromBlog.get("blogLinks");
			resContent += "\r\r■ 연관키워드";
			resContent += searchWordService.getRelatedSearches(searchWord[1]);

		}else {
			resContent = "이해가 가지 않습니다. 다시 한번 얘기해주세요~";
		}
		MessageVO mes = new MessageVO(resContent);
		ChatResponseVO res = new ChatResponseVO();
		res.setMessage(mes);
		
		return res;
	}
}
