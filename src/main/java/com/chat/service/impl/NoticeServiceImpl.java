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

/**
 * 通知逻辑类
 */
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

	/**
	 * 获取通知实体类
	 * @param userId 用户ID
	 * @return 实体类
	 */
	@Override
	public Notice getNoticeById(String userId) {
		Optional<Notice> byId = noticeRepository.findById(userId);
		Notice notice = null;
		if (byId.isPresent()) {
			notice = byId.get();
		}
		return notice;
	}

	/**
	 * 获取通知
	 * @param userId 用户ID
	 * @return 通知列表
	 */
	@Override
	public ReplayNotice getReplayNotice(String userId) {
		ReplayNotice replay = new ReplayNotice();
		Notice noticeById = getNoticeById(userId);
		replay.setNotice(noticeById.getNotice());
		return replay;
	}

	/**
	 * 请求好友通知同意
	 * @param userId 用户ID
	 * @param oppositeId 对方用户ID
	 * @param status 状态码
	 * @param groupName 好友分组
	 * @param oppositeGroupName 对方用户好友分组
	 */
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

	/**
	 * 请求添加群聊通知同意
	 * @param userId 用户ID
	 * @param oppositeId 群聊ID
	 * @param status 状态码
	 */
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

	/**
	 * 请求好友通知拒绝
	 * @param userId 用户ID
	 * @param oppositeId 对方用户ID
	 * @param status 状态码
	 */
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

	/**
	 * 请求添加群聊通知拒绝
	 * @param userId 用户ID
	 * @param oppositeId 群聊ID
	 * @param status 状态码
	 */
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
