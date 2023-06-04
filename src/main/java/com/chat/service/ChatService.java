package com.chat.service;

import com.chat.entity.recive.WebSocketMessage;

public interface ChatService {
	void sendToUser(WebSocketMessage message);
	void sendToGroup(WebSocketMessage message);
	void clearFriendRedis(WebSocketMessage message);
	void clearGroupRedis(WebSocketMessage message);
	String getFriendRecord(WebSocketMessage message);
	String getGroupRecord(WebSocketMessage message);
	String getNumberOfFriendRecord(WebSocketMessage message);
	String getNumberOfGroupRecord(WebSocketMessage message);
}
