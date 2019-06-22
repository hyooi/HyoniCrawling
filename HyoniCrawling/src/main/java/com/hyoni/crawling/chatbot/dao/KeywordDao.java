package com.hyoni.crawling.chatbot.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hyoni.crawling.chatbot.vo.Keyword;

public interface KeywordDao extends CrudRepository<Keyword, Long>{
	List<Keyword> findAll();
}
