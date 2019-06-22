package com.hyoni.crawling.chatbot.vo;

public class ChatResponse {
	private Message message;

	public ChatResponse(String message) {
		this.message = new Message(message);
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
