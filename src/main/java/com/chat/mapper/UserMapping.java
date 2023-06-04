package com.chat.mapper;

import com.chat.entity.User;

public interface UserMapping {
	void appendGroup(String userId, String groupId);
	void appendFriendGroup(String userId, String groupName);
	void appendFriend(String userId, String groupName, String friendId, String friendEachId, String friendName, String avatar);
	void deleteFriend(String userId, String groupName, String friendId);
	void deleteFriendGroup(String userId, String groupName);
	void deleteGroup(String userId, String groupId);
}
