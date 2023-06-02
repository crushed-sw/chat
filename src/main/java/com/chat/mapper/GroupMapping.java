package com.chat.mapper;

import com.chat.entity.Chitchat;
import com.chat.entity.User;

public interface GroupMapping {
	void appendCrew(String groupId, String userId);
	void deleteCrew(String groupId, String userId);
	void appendRecord(String groupId, User user, String message, String date);
	void updateName(String groupId, String newName);
}
