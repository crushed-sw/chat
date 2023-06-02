package com.chat.service;


import com.chat.entity.GroupChatRecord;
import com.chat.entity.UserInGroup;
import com.chat.entity.replay.ReplayGroup;

import java.util.List;

public interface GroupService {
	void insertGroup(String userId, String groupId);
	void createGroup(String userId, String groupName);
	List<UserInGroup> getgroupsById(String userId);
	void deleteGroupById(String userId, String groupId);
	GroupChatRecord getGroupRecordById(String groupId);
	ReplayGroup getReplayGroup(String userId);
	boolean addGroup(String userId, String groupId);
	int isInGroup(String userId, String groupId);
}
