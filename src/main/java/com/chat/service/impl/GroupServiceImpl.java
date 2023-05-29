package com.chat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chat.entity.GroupChatRecord;
import com.chat.entity.ReplayGroup;
import com.chat.entity.User;
import com.chat.mapper.GroupMapping;
import com.chat.mapper.GroupRepository;
import com.chat.mapper.UserMapping;
import com.chat.mapper.UserRepository;
import com.chat.service.GroupService;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserMapping userMapping;
	@Autowired
	GroupMapping groupMapping;
	@Autowired
	GroupRepository groupRepository;

	@Autowired
	UserService userService;

	@Override
	public void insertGroup(String userId, String groupId) {
		groupMapping.appendCrew(userId, groupId);
	}

	@Override
	public void createGroup(String userId, String groupName) {
		String groupId = CommondUtil.getGroupId();
		CommondUtil.incGroupId();

		groupRepository.save(new GroupChatRecord(groupId, userId, "", groupName, new ArrayList<>(), new ArrayList<>()));
		userMapping.appendGroup(userId, groupId);
	}

	@Override
	public List<String> getgroupsById(String userId) {
		User user = userService.getUserById(userId);
		return user.getGroups();
	}

	@Override
	public void deleteGroupById(String userId, String groupId) {
		GroupChatRecord groupRecordById = getGroupRecordById(groupId);
		if(groupRecordById.getOwner().equals(userId)) {
			destroyGroup(groupId);
		} else {
			userMapping.deleteGroup(userId, groupId);
			groupMapping.deleteCrew(groupId, userId);
		}
	}

	@Override
	public void updateGroupById(String groupId, String newGroupName) {
		groupMapping.updateName(groupId, newGroupName);
	}

	public void destroyGroup(String groupId) {
		GroupChatRecord groupRecordB = getGroupRecordById(groupId);
		List<String> crews = groupRecordB.getCrew();
		for (String crew : crews) {
			userMapping.deleteGroup(crew, groupId);
		}
		userMapping.deleteGroup(groupRecordB.getOwner(), groupId);
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
}
