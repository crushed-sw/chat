package com.chat.mapper;

import com.chat.entity.Chitchat;
import com.chat.entity.FriendChatRecord;
import com.chat.entity.GroupChatRecord;
import com.chat.entity.User;

public interface GroupMapping {
	void appendCrew(String groupId, String userId);
	void deleteCrew(String groupId, String userId);
	void appendRecord(String groupId, User user, String message, String date);
	GroupChatRecord getRecord(String groupId, int start, int number);
	int getSizeOfRecord(String groupId);
}
