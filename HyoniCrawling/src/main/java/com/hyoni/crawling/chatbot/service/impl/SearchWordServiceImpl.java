package com.hyoni.crawling.chatbot.service.impl;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyoni.crawling.chatbot.dao.SearchWordDao;
import com.hyoni.crawling.chatbot.service.SearchWordService;

@Service
public class SearchWordServiceImpl implements SearchWordService {	
	
	@Autowired
	private SearchWordDao searchWordDao;

	@Override
	public HashMap<String, Object> getSearchWordFromBlog(String searchWord) throws Exception {
		return searchWordDao.getSearchWordFromBlog(searchWord);
	}

	@Override
	public int getWebDocsCnt(String searchWord) throws Exception {
		return searchWordDao.getWebDocsCnt(searchWord);
	}

	@Override
	public String getRelatedSearches(String searchWord) {
		return searchWordDao.getRelatedSearches(searchWord);
	}

	@Override
	public String getAmountSearches(String searchWord) throws Exception {
		DecimalFormat commas = new DecimalFormat("#,###");
		HashMap<String, Object> map = searchWordDao.getAmountSearches(searchWord);
		map.put("monthlyPcQcCnt", map.get("monthlyPcQcCnt").toString().startsWith("<")? 10: map.get("monthlyPcQcCnt"));
		map.put("monthlyMobileQcCnt", map.get("monthlyMobileQcCnt").toString().startsWith("<")? 10: map.get("monthlyMobileQcCnt"));
		map.put("pcMobileCntSum", (int)map.get("monthlyPcQcCnt")+(int)map.get("monthlyMobileQcCnt"));
		
		String resContent = "\r└ 피　씨 : "+commas.format(map.get("monthlyPcQcCnt"))+" 회";
		resContent += "\r└ 모바일 : "+commas.format(map.get("monthlyMobileQcCnt"))+" 회";
		resContent += "\r└ 합　계 : "+commas.format(map.get("pcMobileCntSum"))+" 회";
		
		return resContent;
	}

	@Override
	public String getSearchRandomKeyword() {
		/*
		 * https://manage.searchad.naver.com/keywordstool?format=json&siteId=&mobileSiteId=&hintKeywords=&includeHintKeywords=0&showDetail=1&biztpId=&mobileBiztpId=&month=12&event=&keyword=
		 * 1. 네이버 광고시스템에서 월간 데이터 불러오기
		 * 2. PC+MOBILE 검색량 합계 2천 이상인 경우 루프돌면서 검색API태울 것
		 * 3. 블로그 문서수 300이하의 키워드 찾으면 RETURN
		 * 4. 시간 오래걸리는 작업이니 배치성 작업으로 처리해두고(30~1시간), 요청하면 RETURN해주는 방식으로 처리
		 * */
		return "랜덤키워드";
	}
}
