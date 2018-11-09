package com.hyoni.crawling.service.impl;

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
}
