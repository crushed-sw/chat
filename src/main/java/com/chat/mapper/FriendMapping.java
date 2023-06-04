package com.chat.mapper;

import com.chat.entity.FriendChatRecord;
import com.chat.entity.User;

public interface FriendMapping {
	void appendRecord(String eachId, User user, String message, String date);
	FriendChatRecord getRecord(String eachId,int start, int number);
	int getSizeOfRecord(String eachId);
}
