package com.chat.service.impl;

import com.chat.entity.GroupChatRecord;
import com.chat.entity.NoticeMessage;
import com.chat.entity.UserInGroup;
import com.chat.entity.replay.ReplayGroup;
import com.chat.entity.User;
import com.chat.entity.replay.ReplayWebSocket;
import com.chat.mapper.*;
import com.chat.service.GroupService;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import com.chat.util.RedisUtil;
import com.chat.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 群聊逻辑类
 */
@Service
public class GroupServiceImpl implements GroupService {
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	CommondUtil commondUtil;
	@Autowired
	UserMapping userMapping;
	@Autowired
	GroupMapping groupMapping;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	NoticeMapping noticeMapping;

	@Autowired
	UserService userService;

	@Autowired
	WebSocket webSocket;

	/**
	 * 添加群聊
	 * @param userId 用户ID
	 * @param groupId 群聊ID
	 */
	@Override
	public void insertGroup(String userId, String groupId) {
		userMapping.appendGroup(userId, groupId);
		groupMapping.appendCrew(groupId, userId);
	}

	/**
	 * 创建群聊
	 * @param userId 用户ID
	 * @param groupName 群聊名称
	 */
	@Override
	public void createGroup(String userId, String groupName) {
		String groupId = "100001";
		try {
			groupId = (String) redisUtil.get("groupId");
		} catch (Exception e) {
			redisUtil.set("groupId", groupId);
		}

		if(groupId == null) {
			groupId = "100001";
			redisUtil.set("groupId", groupId);
		}

		int id = Integer.parseInt(groupId);
		id++;
		redisUtil.set("groupId", Integer.toString(id));
		groupRepository.save(new GroupChatRecord(groupId, userId, CommondUtil.getGroupAvatar(), groupName, new ArrayList<>(), new ArrayList<>()));
		userMapping.appendGroup(userId, groupId);
	}

	/**
	 * 获取群聊列表
	 * @param userId 用户ID
	 * @return 群聊列表
	 */
	@Override
	public List<UserInGroup> getgroupsById(String userId) {
		User user = userService.getUserById(userId);
		List<String> list = user.getGroups();
		List<UserInGroup> replay = new ArrayList<>();
		for (String id : list) {
			UserInGroup userInGroup = new UserInGroup();
			Optional<GroupChatRecord> byId = groupRepository.findById(id);
			GroupChatRecord group = null;
			if(byId.isPresent()) {
				group = byId.get();
			}
			assert (group != null);
			userInGroup.setGroupId(id);
			userInGroup.setName(group.getName());
			userInGroup.setAvatar(group.getAvatar());
			userInGroup.setOwner(group.getOwner());
			replay.add(userInGroup);
		}
		return replay;
	}

	/**
	 * 删除群聊
	 * @param userId 用户ID
	 * @param groupId 群聊ID
	 */
	@Override
	public void deleteGroupById(String userId, String groupId) {
		GroupChatRecord groupRecordById = getGroupRecordById(groupId);
		if(groupRecordById.getOwner().equals(userId)) {
			destroyGroup(groupId);
		} else {
			User userById = userService.getUserById(userId);
			NoticeMessage message = new NoticeMessage(NoticeMessage.MESSAGE_NOTICE,
					userId, userById.getAvatar(), userById.getUserName(), "退出群聊");
			noticeMapping.addMessage(groupRecordById.getOwner(), message);

			userMapping.deleteGroup(userId, groupId);
			groupMapping.deleteCrew(groupId, userId);

			ReplayWebSocket replay = new ReplayWebSocket();
			replay.setStatus(ReplayWebSocket.UPDATE_NOTICE);
			webSocket.sendRemind(groupRecordById.getOwner(), replay);

			redisUtil.delete("chat-" + userId, "group-" + groupId);
		}
	}

	/**
	 * 群主解散群聊
	 * @param groupId 群聊ID
	 */
	public void destroyGroup(String groupId) {
		GroupChatRecord groupRecord = getGroupRecordById(groupId);
		List<String> crews = groupRecord.getCrew();
		ReplayWebSocket replay = new ReplayWebSocket();
		for (String crew : crews) {
			userMapping.deleteGroup(crew, groupId);

			NoticeMessage message = new NoticeMessage(NoticeMessage.MESSAGE_NOTICE,
					groupId, groupRecord.getAvatar(), groupRecord.getName(), "该群已被解散");
			noticeMapping.addMessage(crew, message);

			replay.setStatus(ReplayWebSocket.UPDATE_NOTICE);
			webSocket.sendRemind(crew, replay);

			replay.setId(groupId);
			replay.setStatus(ReplayWebSocket.UPDATE_GROUP);
			webSocket.sendRemind(crew, replay);

			redisUtil.delete("chat-" + crew, "group-" + groupId);
		}

		userMapping.deleteGroup(groupRecord.getOwner(), groupId);
		groupRepository.deleteById(groupId);
		redisUtil.delete("chat-" + groupRecord.getOwner(), "group-" + groupId);
	}

	/**
	 * 获取群聊聊天记录
	 * @param groupId 群聊ID
	 * @return 聊天记录
	 */
	@Override
	public GroupChatRecord getGroupRecordById(String groupId) {
		Optional<GroupChatRecord> byId = groupRepository.findById(groupId);
		GroupChatRecord groupChat = null;
		if(byId.isPresent()) {
			groupChat = byId.get();
		}
		return groupChat;
	}

	/**
	 * 获取群聊列表
	 * @param userId 用户ID
	 * @return 群聊列表
	 */
	@Override
	public ReplayGroup getReplayGroup(String userId) {
		ReplayGroup replay = new ReplayGroup();
		replay.setGroups(getgroupsById(userId));
		return replay;
	}

	/**
	 * 请求添加群聊
	 * @param userId 用户ID
	 * @param groupId 群聊ID
	 * @return 是否成功
	 */
	@Override
	public boolean addGroup(String userId, String groupId) {
		GroupChatRecord groupRecordById = getGroupRecordById(groupId);
		if(groupRecordById == null) {
			return false;
		}
		User user = userService.getUserById(userId);
		NoticeMessage message = new NoticeMessage(NoticeMessage.ADD_GROUP, userId,
				user.getAvatar(), user.getUserName(), "请求添加群聊", "", groupId);
		noticeMapping.addMessage(groupRecordById.getOwner(), message);

		ReplayWebSocket replay = new ReplayWebSocket();
		replay.setStatus(ReplayWebSocket.UPDATE_NOTICE);
		webSocket.sendRemind(groupRecordById.getOwner(), replay);

		return true;
	}

	/**
	 * 判断该用户是否是群聊成员
	 * @param userId 用户ID
	 * @param groupId 群聊ID
	 * @return 是否是群聊成员
	 */
	@Override
	public int isInGroup(String userId, String groupId) {
		GroupChatRecord group = getGroupRecordById(groupId);
		if(group == null) {
			return -3;
		}

		if(group.getOwner().equals(userId)) {
			return -1;
		}

		for (String crew : group.getCrew()) {
			if(crew.equals(userId)) {
				return -2;
			}
		}

		return 0;
	}
}
