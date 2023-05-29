package com.chat.mapper;

import com.chat.entity.Chitchat;

public interface GroupMapping {
	void appendCrew(String groupId, String userId);
	void deleteCrew(String groupId, String userId);
	void appendRecord(String groupId, String userId, String message);
	void updateName(String groupId, String newName);
}
