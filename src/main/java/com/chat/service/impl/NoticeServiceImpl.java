package com.chat.service.impl;

import com.chat.entity.GroupChatRecord;
import com.chat.entity.Notice;
import com.chat.entity.NoticeMessage;
import com.chat.entity.User;
import com.chat.entity.replay.ReplayNotice;
import com.chat.entity.replay.ReplayWebSocket;
import com.chat.mapper.NoticeMapping;
import com.chat.mapper.NoticeRepository;
import com.chat.service.FriendService;
import com.chat.service.GroupService;
import com.chat.service.NoticeService;
import com.chat.service.UserService;
import com.chat.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	NoticeRepository noticeRepository;
	@Autowired
	NoticeMapping noticeMapping;
	@Autowired
	FriendService friendService;
	@Autowired
	GroupService groupService;
	@Autowired
	UserService userService;

	@Autowired
	WebSocket webSocket;

	@Override
	public Notice getNoticeById(String userId) {
		Optional<Notice> byId = noticeRepository.findById(userId);
		Notice notice = null;
		if (byId.isPresent()) {
			notice = byId.get();
		}
		return notice;
	}

	@Override
	public ReplayNotice getReplayNotice(String userId) {
		ReplayNotice replay = new ReplayNotice();
		Notice noticeById = getNoticeById(userId);
		replay.setNotice(noticeById.getNotice());
		return replay;
	}

	@Override
	public void addFriend(String userId, String oppositeId, Integer status, String groupName, String oppositeGroupName) {
		noticeMapping.updateMessage(userId, oppositeId, status, NoticeMessage.MESSAGE_ADDED);
		friendService.insertFriend(userId, oppositeId, groupName, oppositeGroupName);

		User user = userService.getUserById(userId);
		NoticeMessage noticeMessage = new NoticeMessage(NoticeMessage.MESSAGE_NOTICE,
				userId, user.getAvatar(), user.getUserName(), "对方已同意添加为好友");
		noticeMapping.addMessage(oppositeId, noticeMessage);

		ReplayWebSocket replay = new ReplayWebSocket();
		replay.setStatus(ReplayWebSocket.UPDATE_NOTICE);
		webSocket.sendRemind(oppositeId, replay);
		replay.setStatus(ReplayWebSocket.UPDATE_FRIEND);
		webSocket.sendRemind(oppositeId, replay);
	}

	@Override
	public void addGroup(String userId, String oppositeId, Integer status) {
		GroupChatRecord group = groupService.getGroupRecordById(oppositeId);
		noticeMapping.updateMessage(group.getOwner(), userId, status, NoticeMessage.MESSAGE_ADDED);
		groupService.insertGroup(userId, oppositeId);

		NoticeMessage noticeMessage = new NoticeMessage(NoticeMessage.MESSAGE_NOTICE,
				oppositeId, group.getAvatar(), group.getName(), "群主已同意加入群聊");
		noticeMapping.addMessage(userId, noticeMessage);

		ReplayWebSocket replay = new ReplayWebSocket();
		replay.setStatus(ReplayWebSocket.UPDATE_NOTICE);
		webSocket.sendRemind(userId, replay);
		replay.setStatus(ReplayWebSocket.UPDATE_GROUP);
		webSocket.sendRemind(userId, replay);
	}

	@Override
	public void refuseFriend(String userId, String oppositeId, Integer status) {
		noticeMapping.updateMessage(userId, oppositeId, status, NoticeMessage.MESSAGE_REJECTED);

		User user = userService.getUserById(userId);
		NoticeMessage noticeMessage = new NoticeMessage(NoticeMessage.MESSAGE_NOTICE,
				userId, user.getAvatar(), user.getUserName(), "对方拒绝添加好友");
		noticeMapping.addMessage(oppositeId, noticeMessage);

		ReplayWebSocket replay = new ReplayWebSocket();
		replay.setStatus(ReplayWebSocket.UPDATE_NOTICE);
		webSocket.sendRemind(oppositeId, replay);
	}

	@Override
	public void refuseGroup(String userId, String oppositeId, Integer status) {
		GroupChatRecord group = groupService.getGroupRecordById(oppositeId);
		noticeMapping.updateMessage(group.getOwner(), userId, status, NoticeMessage.MESSAGE_REJECTED);

		NoticeMessage noticeMessage = new NoticeMessage(NoticeMessage.MESSAGE_NOTICE,
				userId, group.getAvatar(), group.getName(), "对方拒绝您加入群聊");
		noticeMapping.addMessage(userId, noticeMessage);

		ReplayWebSocket replay = new ReplayWebSocket();
		replay.setStatus(ReplayWebSocket.UPDATE_NOTICE);
		webSocket.sendRemind(userId, replay);
	}
}
