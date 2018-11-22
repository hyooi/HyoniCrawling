package com.hyoni.crawling.controller;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
public class ChatbotController {
	private static final Logger logger = LoggerFactory.getLogger(ChatbotController.class);
	
	@Autowired
	private WeatherService weatherService;
	
	@Autowired
	private SearchWordService searchWordService;
	
	@Autowired
	private OtherUtilService otherUtilService;
	
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET)
	public KeyboardVO keyboard() {
		logger.info("[KEYBOARD] 접속");
		
		return new KeyboardVO();
	}
	
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public ChatResponseVO chatRequest(@RequestBody ChatRequestVO req) throws Exception {
		String reqContent = req.getContent();
		String resContent;
		
		logger.info("[MESSAGE] USER : " + req.getUser_key() + " | REQUEST : " + reqContent);
		
		if ((reqContent.contains("안녕"))||(reqContent.contains("하이")||(reqContent.contains("hi")))){
			resContent = "안녕하세요 :)";
		}else if(reqContent.startsWith("!도움말")){
			resContent = otherUtilService.getHelpMessage();
		}else if(reqContent.startsWith("!날씨")) {
			resContent = weatherService.getWeathers();
		}else if(reqContent.startsWith("!검색")) {
			String searchWord = reqContent.substring(3).trim();
			if(searchWord.equals(""))	return new ChatResponseVO("검색어를 입력해주세요.");
			
			HashMap<String, Object> resultDataFromBlog = searchWordService.getSearchWordFromBlog(searchWord);
			DecimalFormat commas = new DecimalFormat("#,###");
			
			resContent = "■ 키워드 : "+searchWord;
			resContent += searchWordService.getAmountSearches(searchWord.replaceAll("\\p{Z}", ""));
			resContent += "\r└ 웹문서 : "+commas.format(searchWordService.getWebDocsCnt(searchWord))+" 회";
			resContent += "\r└ 블로그 : "+commas.format(resultDataFromBlog.get("blogTotal"))+" 건";
			resContent += "\r└ 순　위 : "+resultDataFromBlog.get("blogLinks");
			resContent += "\r\r■ 연관키워드";
			resContent += searchWordService.getRelatedSearches(searchWord);
		}else if(reqContent.startsWith("!로또번호")){
			resContent = otherUtilService.getLottoNumbers(req.getUser_key());
		}else if(reqContent.equals("!랜덤키워드")){
			resContent = searchWordService.getSearchRandomKeyword();
		}else{
			resContent = otherUtilService.getOtherRequest();
		}
		
		return new ChatResponseVO(resContent);
	}
}
