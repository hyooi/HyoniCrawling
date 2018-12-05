package com.hyoni.crawling.chatbot.service;

import java.util.HashMap;

public interface SearchWordService {
	HashMap<String, Object> getSearchWordFromBlog(String searchWord) throws Exception;
	int getWebDocsCnt(String searchWord) throws Exception;
	String getRelatedSearches(String searchWord);
	String getAmountSearches(String searchWord) throws Exception;
	String getSearchRandomKeyword();
}
