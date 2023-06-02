package com.chat.mapper;

import com.chat.entity.NoticeMessage;

public interface NoticeMapping {
	void addMessage(String userId, NoticeMessage message);
	void updateMessage(String userId, String fromId, Integer status, Integer newStatus);
}
