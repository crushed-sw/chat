package com.chat.service;


import com.chat.entity.GroupChatRecord;
import com.chat.entity.ReplayGroup;
import com.chat.entity.UserInFriend;

import java.util.List;

public interface GroupService {
	void insertGroup(String userId, String groupId);
	void createGroup(String userId, String groupName);
	List<String> getgroupsById(String userId);
	void deleteGroupById(String userId, String groupId);
	void updateGroupById(String groupId, String newGroupName);
	GroupChatRecord getGroupRecordById(String groupId);
	ReplayGroup getReplayGroup(String userId);
}
