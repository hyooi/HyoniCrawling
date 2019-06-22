package com.hyoni.crawling.chatbot.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Top_rnd_keyword")
public class Keyword implements Serializable {
	private static final long serialVersionUID = -3869042861262315239L;
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long seq;

	private String keyword;
	private long monthlySearchCnt;
	private long blogCnt;
	private Date inp_date;
	private String inp_name;
	
	public Keyword() {}
	public Keyword(String keyword, long monthlySearchCnt, long blogCnt) {
		this.keyword = keyword;
		this.monthlySearchCnt = monthlySearchCnt;
		this.blogCnt = blogCnt;
		this.inp_name = "전산실";
	}
	
	public long getSeq() {
		return seq;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public long getMonthlySearchCnt() {
		return monthlySearchCnt;
	}
	public void setMonthlySearchCnt(long monthlySearchCnt) {
		this.monthlySearchCnt = monthlySearchCnt;
	}
	public long getBlogCnt() {
		return blogCnt;
	}
	public void setBlogCnt(long blogCnt) {
		this.blogCnt = blogCnt;
	}
	public Date getInp_date() {
		return inp_date;
	}
	public String getInp_name() {
		return inp_name;
	}
}
