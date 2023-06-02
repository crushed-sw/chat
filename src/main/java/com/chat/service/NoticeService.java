package com.chat.service;

import com.chat.entity.Notice;
import com.chat.entity.replay.ReplayNotice;

public interface NoticeService {
	Notice getNoticeById(String userId);
	ReplayNotice getReplayNotice(String userId);
	void addFriend(String userId, String oppositeId, Integer status, String groupName, String oppositeGroupName);
	void addGroup(String userId, String oppositeId, Integer status);
	void refuseFriend(String userId, String oppositeId, Integer status);
	void refuseGroup(String userId, String oppositeId, Integer status);
}
