package com.hyoni.crawling;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CrawlingController {
	@RequestMapping("/test")
	public String testCrawlling() {
		System.out.println("dddd");
		return "/test/test";
	}
}
