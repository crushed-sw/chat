package com.chat.mapper;

public interface UserMapping {
	void appendGroup(String userId, String groupId);
	void appendFriendGroup(String userId, String groupName);
	void appendFriend(String userId, String groupName, String friendId, String remarks, String friendEachId);
}
