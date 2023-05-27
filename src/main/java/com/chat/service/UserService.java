package com.chat.service;

import com.chat.entity.User;

public interface UserService {
	void insertUser(String userId, String userName, String password);
	User getUserById(String userId);
	void deleteById(String userId);
	void updateUserById(User user);
	void insertFriend(String groupName, String userId);
	void deleteFriend(String userId);
	void updateFriend(String oldGroupName, String newGroupName, String userId);
	void insertGroup(String groupId);
	void deleteGroup(String groupId);
	void updateGroup(String groupId, String groupName);
}
