package com.chat.service;

import com.chat.entity.replay.ReplayFriend;
import com.chat.entity.UserInFriend;

import java.util.List;

public interface FriendService {
	void insertFriend(String userId, String friendId, String groupName, String friendGroupName);
	List<UserInFriend> getFriendsById(String userId);
	void deleteFriendById(String userId, String groupName, String friendId);
	void updateFriendById(String userId, String oldGroupName, String newGroupName, String friendId);
	ReplayFriend getReplayFriend(String userId);
	boolean insertFriendGroup(String userId, String groupName);
	void deleteFriendGroup(String userId, String groupName);
	boolean addFriend(String userId, String friendId, String groupName);
	boolean isFriend(String userId, String friendId);
}
