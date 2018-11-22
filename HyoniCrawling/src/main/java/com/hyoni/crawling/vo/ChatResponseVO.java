package com.hyoni.crawling.vo;

public class ChatResponseVO {
	private MessageVO message;

	public ChatResponseVO(String message) {
		this.message = new MessageVO(message);
	}

	public MessageVO getMessage() {
		return message;
	}

	public void setMessage(MessageVO message) {
		this.message = message;
	}
}
