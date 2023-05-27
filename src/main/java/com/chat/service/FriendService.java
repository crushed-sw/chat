package com.chat.service;

import com.chat.entity.User;
import com.chat.entity.UserInFriend;

import java.util.List;

public interface FriendService {
	void insertFriend(String userId, String friendId, String groupName);
	List<UserInFriend> getFriendsById(String userId);
	void deleteById(String userId);
	void updateUserById(User user);
}
