package com.hyoni.crawling.chatbot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyoni.crawling.chatbot.dao.OtherUtilDao;
import com.hyoni.crawling.chatbot.service.OtherUtilService;

@Service
public class OtherUtilServiceImpl implements OtherUtilService {
	
	@Autowired
	private OtherUtilDao otherUtilDao;
	
	@Override
	public String getLottoNumbers(String userKey) {
		String lottoNumbers;
		lottoNumbers = userKey + "님께서 뽑으신 로또번호입니다.";
		lottoNumbers += "\r1번) "+otherUtilDao.getLottoNumber();
		lottoNumbers += "\r2번) "+otherUtilDao.getLottoNumber();
		lottoNumbers += "\r3번) "+otherUtilDao.getLottoNumber();
		lottoNumbers += "\r4번) "+otherUtilDao.getLottoNumber();
		lottoNumbers += "\r5번) "+otherUtilDao.getLottoNumber();
		lottoNumbers += "\r\r행운이 있기를 바랍니다. 뿅뿅!! ";
		
		return lottoNumbers;
	}

	@Override
	public String getHelpMessage() {
		String resContent = "[도움말]";
		resContent += "\r1. 날씨";
		resContent += "\rEX.!날씨";
		resContent += "\r\r2. 키워드 검색";
		resContent += "\rEX.!검색 심심이";
		resContent += "\r\r3. 로또번호 추출기";
		resContent += "\rEX.!로또번호";
		
		return resContent;
	}

	@Override
	public String getOtherRequest() {
		return "안녕하세요. 심심이입니다.\r명령어는 '!도움말'을 외쳐주세요. :)";
	}	
}
