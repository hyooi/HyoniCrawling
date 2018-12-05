package com.hyoni.crawling.common.vo;

public class NaverAuthVO {
	private String nnb;
	private String token;
	
	public NaverAuthVO(String nnb, String token) {
		this.nnb = nnb;
		this.token = token;
	}
	
	public String getNnb() {
		return nnb;
	}
	public void setNnb(String nnb) {
		this.nnb = nnb;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
