package com.hyoni.crawling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyoni.crawling.dao.OtherUtilDao;
import com.hyoni.crawling.service.OtherUtilService;

@Service
public class OtherUtilServiceImpl implements OtherUtilService {
	
	@Autowired
	private OtherUtilDao otherUtilDao;
	
	@Override
	public String getLottoNumbers() {
		String lottoNumbers;
		lottoNumbers = "\r1번) "+otherUtilDao.getLottoNumber();
		lottoNumbers += "\r2번) "+otherUtilDao.getLottoNumber();
		lottoNumbers += "\r3번) "+otherUtilDao.getLottoNumber();
		lottoNumbers += "\r4번) "+otherUtilDao.getLottoNumber();
		lottoNumbers += "\r5번) "+otherUtilDao.getLottoNumber();
		
		return lottoNumbers;
	}	
}
