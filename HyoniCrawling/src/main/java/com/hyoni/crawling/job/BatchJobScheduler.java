package com.hyoni.crawling.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hyoni.crawling.common.dao.NaverAdsystemDao;

@Component
public class BatchJobScheduler {
	private static final Logger logger = LoggerFactory.getLogger(BatchJobScheduler.class);
	
	@Autowired
	NaverAdsystemDao naverAdsystemDao;
	
	@Scheduled(fixedDelay=600000) //10분 단위로 네이버 광고시스템 로그인
	public void naverAdsystemLoginSchedule() throws Exception {
		logger.info("NaverAdsystem login Schedule");
		naverAdsystemDao.getLoginToNaverAdSystem();
	}
}