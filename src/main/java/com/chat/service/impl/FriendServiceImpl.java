package com.chat.service.impl;

import com.chat.entity.*;
import com.chat.entity.replay.ReplayFriend;
import com.chat.entity.replay.ReplayWebSocket;
import com.chat.mapper.FriendRepository;
import com.chat.mapper.NoticeMapping;
import com.chat.mapper.UserMapping;
import com.chat.service.FriendService;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import com.chat.util.RedisUtil;
import com.chat.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	CommondUtil commondUtil;
	@Autowired
	FriendRepository friendRepository;
	@Autowired
	UserMapping userMapping;
	@Autowired
	NoticeMapping noticeMapping;

	@Autowired
	UserService userService;

	@Autowired
	WebSocket webSocket;

	/**
	 * 添加好友
	 * @param userId 用户ID
	 * @param friendId 好友ID
	 * @param groupName 好友分组
	 * @param friendGroupName 好友的好友分组
	 */
	@Override
	public void insertFriend(String userId, String friendId, String groupName, String friendGroupName) {
		String eachId = "1000001";
		try {
			eachId = (String) redisUtil.get("friendEachId");
		} catch (Exception e) {
			redisUtil.set("friendEachId", eachId);
		}

		if(eachId == null) {
			eachId = "1000001";
			redisUtil.set("groupId", eachId);
		}

		int id = Integer.parseInt(eachId);
		id++;
		redisUtil.set("friendEachId", Integer.toString(id));
		friendRepository.save(new FriendChatRecord(eachId, userId, friendId));

		User user = userService.getUserById(userId);
		User friend = userService.getUserById(friendId);

		userMapping.appendFriend(userId, groupName, friendId, eachId, friend.getUserName(), friend.getAvatar());
		userMapping.appendFriend(friendId, friendGroupName, userId, eachId, user.getUserName(), user.getAvatar());


	}

	/**
	 * 获取好友列表
	 * @param userId 用户ID
	 * @return 好友列表
	 */
	@Override
	public List<UserInFriend> getFriendsById(String userId) {
		User user = userService.getUserById(userId);

		assert user != null;
		return user.getFriendGroups();
	}

	/**
	 * 删除好友
	 * @param userId 用户ID
	 * @param groupName 好友分组
	 * @param friendId 好友ID
	 */
	@Override
	public void deleteFriendById(String userId, String groupName, String friendId) {
		User friend = userService.getUserById(friendId);
		assert friend != null;
		List<UserInFriend> friendGroups = friend.getFriendGroups();

		String eachId = "";
		String friendGroupName = "";

		for (UserInFriend friendGroup : friendGroups) {
			for (Friend friendFriend : friendGroup.getFriends()) {
				if (userId.equals(friendFriend.getFriendId())) {
					eachId = friendFriend.getFriendEachId();
					friendGroupName = friendGroup.getGroupName();
				}
			}
		}

		friendRepository.deleteById(eachId);
		userMapping.deleteFriend(userId, groupName, friendId);
		userMapping.deleteFriend(friendId, friendGroupName, userId);

		ReplayWebSocket replay = new ReplayWebSocket();
		replay.setStatus(ReplayWebSocket.UPDATE_FRIEND);
		replay.setId(userId);
		webSocket.sendRemind(friendId, replay);

		redisUtil.delete("chat-" + friendId, "friend-" + userId);
		redisUtil.delete("chat-" + userId, "friend-" + friendId);
	}

	/**
	 * 将好友更换好友分组
	 * @param userId 用户ID
	 * @param oldGroupName 旧好友分组
	 * @param newGroupName 新好友分许
	 * @param friendId 好友ID
	 */
	@Override
	public void updateFriendById(String userId, String oldGroupName, String newGroupName, String friendId) {
		User user = userService.getUserById(userId);

		String eachId = "";
		String avatar = "";
		String friendName = "";
		List<UserInFriend> userGroups = user.getFriendGroups();

		for (UserInFriend userGroup : userGroups) {
			for (Friend friend : userGroup.getFriends()) {
				if (friendId.equals(friend.getFriendId())) {
					eachId = friend.getFriendEachId();
					avatar = friend.getAvatar();
					friendName = friend.getFriendName();
				}
			}
		}

		userMapping.deleteFriend(userId, oldGroupName, friendId);
		userMapping.appendFriend(userId, newGroupName, friendId, eachId, friendName, avatar);
	}

	/**
	 * 获取好友列表
	 * @param userId 用户ID
	 * @return 好友列表
	 */
	@Override
	public ReplayFriend getReplayFriend(String userId) {
		ReplayFriend replay = new ReplayFriend();
		replay.setFriendList(getFriendsById(userId));
		return replay;
	}

	/**
	 * 添加好友分组
	 * @param userId 用户ID
	 * @param groupName 好友分组
	 * @return 是否成功
	 */
	@Override
	public boolean insertFriendGroup(String userId, String groupName) {
		for (UserInFriend userInFriend : getFriendsById(userId)) {
			if (userInFriend.getGroupName().equals(groupName.trim())) {
				return false;
			}
		}

		userMapping.appendFriendGroup(userId, groupName);
		return true;
	}

	/**
	 * 删除好友分组
	 * @param userId 用户ID
	 * @param groupName 好友分组
	 */
	@Override
	public void deleteFriendGroup(String userId, String groupName) {
		if (!"我的好友".equals(groupName.trim())) {
			userMapping.deleteFriendGroup(userId, groupName);
		}
	}

	/**
	 * 申请添加好友
	 * @param userId 用户ID
	 * @param friendId 好友ID
	 * @param groupName 好友分组
	 * @return 是否成功
	 */
	@Override
	public boolean addFriend(String userId, String friendId, String groupName) {
		User friend = userService.getUserById(friendId);
		if (friend == null) {
			return false;
		}
		User user = userService.getUserById(userId);
		NoticeMessage message = new NoticeMessage(NoticeMessage.ADD_FRIEND, userId,
				user.getAvatar(), user.getUserName(), "请求添加好友", groupName, "");
		noticeMapping.addMessage(friendId, message);

		ReplayWebSocket replay = new ReplayWebSocket();
		replay.setStatus(ReplayWebSocket.UPDATE_NOTICE);
		webSocket.sendRemind(friendId, replay);

		return true;
	}

	/**
	 * 判断俩用户之间是不是好友
	 * @param userId 用户ID
	 * @param friendId 好友ID
	 * @return 返回
	 */
	@Override
	public boolean isFriend(String userId, String friendId) {
		User user = userService.getUserById(userId);
		for (UserInFriend friendGroup : user.getFriendGroups()) {
			for (Friend friend : friendGroup.getFriends()) {
				if (friend.getFriendId().equals(friendId.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取两个好友间的聊天ID
	 * @param userId 用户ID
	 * @param friendId 好友ID
	 * @return 返回聊天ID
	 */
	@Override
	public String getEachId(String userId, String friendId) {
		User friend = userService.getUserById(friendId);
		assert (friend != null);
		for (UserInFriend userInFriend : friend.getFriendGroups()) {
			for (Friend friendUser : userInFriend.getFriends()) {
				if (friendUser.getFriendId().equals(userId)) {
					return friendUser.getFriendEachId();
				}
			}
		}
		return "";
	}
}