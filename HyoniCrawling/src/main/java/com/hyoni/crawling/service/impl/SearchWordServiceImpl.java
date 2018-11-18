package com.hyoni.crawling.service.impl;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyoni.crawling.dao.SearchWordDao;
import com.hyoni.crawling.service.SearchWordService;

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
		
		String resContent = "\r└ 피　씨 : "+commas.format(map.get("monthlyPcQcCnt").toString().startsWith("<")? 10: map.get("monthlyPcQcCnt"))+" 회";
		resContent += "\r└ 모바일 : "+commas.format(map.get("monthlyMobileQcCnt").toString().startsWith("<")? 10: map.get("monthlyMobileQcCnt"))+" 회";
		
		return resContent;
	}
}
