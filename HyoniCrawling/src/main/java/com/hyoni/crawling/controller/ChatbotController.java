package com.hyoni.crawling.controller;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hyoni.crawling.service.OtherUtilService;
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
	
	@Autowired
	private OtherUtilService otherUtilService;
	
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET)
	public KeyboardVO keyboard() {
		KeyboardVO keyboard = new KeyboardVO(new String[] {"안녕", "도움말"});
		
		return keyboard;
	}
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public ChatResponseVO chatRequest(@RequestBody ChatRequestVO req) throws Exception {
		String reqContent = req.getContent();
		String resContent;
		
		if ((reqContent.contains("안녕"))||(reqContent.contains("하이")||(reqContent.contains("hi")))){
			resContent = "안녕하세요~ :)";
		}else if(reqContent.equals("도움말")){
			resContent = "[도움말]";
			resContent += "\r1. 날씨";
			resContent += "\rEX.!날씨, !날씨 서울, !날씨 부산";
			resContent += "\r\r2. 키워드 검색";
			resContent += "\rEX. !검색 심심이";
			resContent += "\r\r3. 로또번호 추출기";
			resContent += "\rEX. !로또번호";
		}
		else if(reqContent.startsWith("!날씨")) {
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
			String searchWord = reqContent.substring(3).trim();
			HashMap<String, Object> resultDataFromBlog = searchWordService.getSearchWordFromBlog(searchWord);
			DecimalFormat commas = new DecimalFormat("#,###");
			
			resContent = "■ 키워드 : "+searchWord;
			resContent += "\r└ 피　씨 : ? 회";
			resContent += "\r└ 모바일 : ? 회";
			resContent += "\r└ 합　계 : "+commas.format(searchWordService.getWebDocsCnt(searchWord))+" 회";
			resContent += "\r└ 블로그 : "+commas.format(resultDataFromBlog.get("blogTotal"))+" 건";
			resContent += "\r└ 순　위 : "+resultDataFromBlog.get("blogLinks");
			resContent += "\r\r■ 연관키워드";
			resContent += searchWordService.getRelatedSearches(searchWord);

		}else if(reqContent.startsWith("!로또번호")){
			resContent = req.getUser_key()+"님께서 뽑으신 로또번호입니다.";
			resContent += otherUtilService.getLottoNumbers();
			resContent += "\r\r행운이 있기를 바랍니다. 뿅뿅!! ";
		}
		else {
			resContent = "이해가 가지 않습니다.";
			resContent += "\r[도움말]을 입력하시면, 사용법을 확인하실 수 있어요~";
		}
		
		MessageVO mes = new MessageVO(resContent);
		ChatResponseVO res = new ChatResponseVO();
		res.setMessage(mes);
		
		return res;
	}
}
