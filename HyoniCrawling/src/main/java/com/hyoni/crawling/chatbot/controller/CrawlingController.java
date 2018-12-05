package com.hyoni.crawling.chatbot.controller;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CrawlingController {
	@RequestMapping("/test")
	public String testCrawlling(Model model) {
		Document doc = null;
		try {
			doc = Jsoup.connect("https://datalab.naver.com/").get();
			model.addAttribute("craw1", doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "/test/test";
	}
}
