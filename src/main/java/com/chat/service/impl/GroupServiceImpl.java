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

@Service
public class GroupServiceImpl implements GroupService {
	@Autowired
	RedisUtil redisUtil;
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


	@Override
	public void insertGroup(String userId, String groupId) {
		userMapping.appendGroup(userId, groupId);
		groupMapping.appendCrew(groupId, userId);
	}

	@Override
	public void createGroup(String userId, String groupName) {
		String groupId = (String) redisUtil.get("groupId");
		int id = Integer.parseInt(groupId);
		id++;
		redisUtil.set("groupId", Integer.toString(id));
		groupRepository.save(new GroupChatRecord(groupId, userId, CommondUtil.getAvatar(), groupName, new ArrayList<>(), new ArrayList<>()));
		userMapping.appendGroup(userId, groupId);
	}

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
		}
	}

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
			replay.setStatus(ReplayWebSocket.UPDATE_GROUP);
			webSocket.sendRemind(crew, replay);
		}

		userMapping.deleteGroup(groupRecord.getOwner(), groupId);
		groupRepository.deleteById(groupId);
	}

	@Override
	public GroupChatRecord getGroupRecordById(String groupId) {
		Optional<GroupChatRecord> byId = groupRepository.findById(groupId);
		GroupChatRecord groupChat = null;
		if(byId.isPresent()) {
			groupChat = byId.get();
		}
		return groupChat;
	}

	@Override
	public ReplayGroup getReplayGroup(String userId) {
		ReplayGroup replay = new ReplayGroup();
		replay.setGroups(getgroupsById(userId));
		return replay;
	}

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
