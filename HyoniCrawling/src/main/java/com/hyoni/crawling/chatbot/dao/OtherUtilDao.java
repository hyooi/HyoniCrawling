package com.hyoni.crawling.chatbot.dao;

import java.util.ArrayList;
import java.util.TreeSet;

import org.springframework.stereotype.Repository;

@Repository("OtherUtilDao")
public class OtherUtilDao {
	public String getLottoNumber() {
		TreeSet<Integer> sortedlottoNum = new TreeSet<>();
		while (true) {
			sortedlottoNum.add((int) (Math.random()*45+1));
			if(sortedlottoNum.size() == 5) break;
		}
		
		ArrayList<Integer> returnLottoNum = new ArrayList<>(sortedlottoNum);
		return returnLottoNum.get(0)+", "+returnLottoNum.get(1)+", "+returnLottoNum.get(2)
				+", "+returnLottoNum.get(3)+", "+returnLottoNum.get(4);
	}
}