package com.hyoni.crawling.chatbot.dao;

import java.util.Arrays;

import org.springframework.stereotype.Repository;

@Repository("OtherUtilDao")
public class OtherUtilDao {
	public String getLottoNumber() {
		int[] lottoNum = new int[6];
		for (int i = 0; i < 6; i++) {
			lottoNum[i] = (int)(Math.random()*45+1);
			if(i==5) Arrays.sort(lottoNum);
		}
		return lottoNum[0]+", "+lottoNum[1]+", "+lottoNum[2]+", "+lottoNum[3]+", "+lottoNum[4]+", "+lottoNum[5];
	}
	
}