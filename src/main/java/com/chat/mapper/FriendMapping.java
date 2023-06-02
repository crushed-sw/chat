package com.chat.mapper;

import com.chat.entity.User;

public interface FriendMapping {
	void appendRecord(String eachId, User user, String message, String date);
}
