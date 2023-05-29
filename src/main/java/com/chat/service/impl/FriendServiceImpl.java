package com.chat.service.impl;

import com.chat.entity.*;
import com.chat.mapper.FriendRepository;
import com.chat.mapper.UserMapping;
import com.chat.mapper.UserRepository;
import com.chat.service.FriendService;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
	@Autowired
	FriendRepository friendRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserMapping userMapping;

	@Autowired
	UserService userService;

	@Override
	public void insertFriend(String userId, String friendId, String groupName, String friendGroupName) {
		String eachId = CommondUtil.getFriendEachId();
		CommondUtil.incFriendEachId();
		friendRepository.save(new FriendChatRecord(eachId, userId, friendId));
		//userMapping.appendFriend(userId, groupName, friendId, eachId);
		//userMapping.appendFriend(friendId, friendGroupName, userId, eachId);
	}

	@Override
	public List<UserInFriend> getFriendsById(String userId) {
		User user = userService.getUserById(userId);

		assert user != null;
		return user.getFriendGroups();
	}

	@Override
	public void deleteFriendById(String userId, String groupName, String friendId) {
		User friend = userService.getUserById(friendId);
		assert friend != null;
		List<UserInFriend> friendGroups = friend.getFriendGroups();

		String eachId = "";
		String friendGroupName = "";

		for (UserInFriend friendGroup : friendGroups) {
			for (Friend friendFriend : friendGroup.getFriends()) {
				if(userId.equals(friendFriend.getFriendId())) {
					eachId = friendFriend.getFriendEachId();
					friendGroupName = friendGroup.getGroupName();
				}
			}
		}

		friendRepository.deleteById(eachId);
		userMapping.deleteFriend(userId, groupName, friendId);
		userMapping.deleteFriend(friendId, friendGroupName, userId);
	}

	@Override
	public void updateFriendById(String userId, String oldGroupName, String newGroupName, String friendId) {
		User user = userService.getUserById(userId);

		String eachId = "";
		String avatar = "";
		String friendName = "";
		List<UserInFriend> userGroups = user.getFriendGroups();

		for (UserInFriend userGroup : userGroups) {
			for (Friend friend : userGroup.getFriends()) {
				if(friendId.equals(friend.getFriendId())) {
					eachId = friend.getFriendEachId();
					avatar = friend.getAvatar();
					friendName = friend.getFriendName();
				}
			}
		}

		userMapping.deleteFriend(userId, oldGroupName, friendId);
		userMapping.appendFriend(userId, newGroupName, friendId, eachId, friendName, avatar);
	}

	public String getEachId(String userId, String friendId) {
		User user = userService.getUserById(userId);

		String eachId = "";
		List<UserInFriend> userGroups = user.getFriendGroups();

		for (UserInFriend userGroup : userGroups) {
			for (Friend friend : userGroup.getFriends()) {
				if(friendId.equals(friend.getFriendId())) {
					eachId = friend.getFriendEachId();
				}
			}
		}

		return eachId;
	}

	@Override
	public ReplayFriend getReplayFriend(String userId) {
		ReplayFriend replay = new ReplayFriend();
		replay.setFriendList(getFriendsById(userId));
		return replay;
	}

	@Override
	public void insertFriendGroup(String userId, String groupName) {
		userMapping.appendFriendGroup(userId, groupName);
	}

	@Override
	public void deleteFriendGroup(String userId, String groupName) {
		if(!"我的好友".equals(groupName.trim())) {
			userMapping.deleteFriendGroup(userId, groupName);
		}
	}

	@Override
	public void updateFriendGroup(String userId, String groupName, String newGroupName) {
		//userMapping.
	}
}
