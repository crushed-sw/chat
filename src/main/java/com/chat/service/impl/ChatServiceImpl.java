package com.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.chat.entity.Chitchat;
import com.chat.entity.FriendChatRecord;
import com.chat.entity.GroupChatRecord;
import com.chat.entity.User;
import com.chat.entity.recive.WebSocketMessage;
import com.chat.entity.replay.ReplayChat;
import com.chat.entity.replay.ReplayWebSocket;
import com.chat.mapper.FriendMapping;
import com.chat.mapper.FriendRepository;
import com.chat.mapper.GroupMapping;
import com.chat.mapper.GroupRepository;
import com.chat.service.ChatService;
import com.chat.service.FriendService;
import com.chat.service.GroupService;
import com.chat.service.UserService;
import com.chat.util.RedisUtil;
import com.chat.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天相关逻辑实现
 */
@Service
public class ChatServiceImpl implements ChatService {
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	FriendRepository friendRepository;

	@Autowired
	FriendService friendService;
	@Autowired
	GroupService groupService;
	@Autowired
	UserService userService;
	@Autowired
	FriendMapping friendMapping;
	@Autowired
	GroupMapping groupMapping;

	@Autowired
	WebSocket webSocket;
	@Autowired
	RedisUtil redisUtil;

	/**
	 * 发送消息给用户
	 * @param message 前端传的JSON
	 *        fromId 当前用户ID
	 *        toId 好友ID
	 *        chat 聊天内容
	 */
	@Override
	public void sendToUser(WebSocketMessage message) {
		String fromId = message.getFromId();
		String toId = message.getToId();
		String chat = message.getMessage();

		User user = userService.getUserById(fromId);
		ReplayWebSocket replay = new ReplayWebSocket(ReplayWebSocket.SEND_TO_USER,
				user.getUserId(), user.getAvatar(), user.getUserName(), chat, message.getDate());
		webSocket.sendUserMessage(toId, replay);

		String eachId = friendService.getEachId(fromId, toId);
		friendMapping.appendRecord(eachId, user, chat, message.getDate());

		clearFriendRedis(message);
	}

	/**
	 * 发送消息给群聊
	 * @param message 前端传的JSON
	 *        fromId 当前用户ID
	 *        toId 群聊ID
	 *        chat 聊天内容
	 */
	@Override
	public void sendToGroup(WebSocketMessage message) {
		String fromId = message.getFromId();
		String toId = message.getToId();
		String chat = message.getMessage();

		User user = userService.getUserById(fromId);
		GroupChatRecord group = groupRepository.findById(toId).get();
		ReplayWebSocket replay = new ReplayWebSocket(ReplayWebSocket.SEND_TO_GROUP,
				group.getGroupId(), user.getAvatar(), user.getUserName(), chat, message.getDate());
		webSocket.sendGroupMessage(toId, fromId, replay);

		groupMapping.appendRecord(toId, user, chat, message.getDate());

		clearGroupRedis(message);
	}

	/**
	 * 清除好友的未读缓存
	 * @param message 前端传的JSON
	 *        fromId 当前用户ID
	 *        toId 好友ID
	 */
	@Override
	public void clearFriendRedis(WebSocketMessage message) {
		String key = "chat-" + message.getFromId();
		String friendKey = "friend-" + message.getToId();
		redisUtil.delete(key, friendKey);
	}

	/**
	 * 清除群聊的未读缓存
	 * @param message 前端传的JSON
	 *        fromId 当前用户ID
	 *        toId 群聊ID
	 */
	@Override
	public void clearGroupRedis(WebSocketMessage message) {
		String key = "chat-" + message.getFromId();
		String friendKey = "group-" + message.getToId();
		redisUtil.delete(key, friendKey);
	}

	/**
	 * 获取好友聊天记录(下载)
	 * @param message
	 * @return
	 */
	@Override
	public String getFriendRecord(WebSocketMessage message) {
		String fromId = message.getFromId();
		String toId = message.getToId();

		String eachId = friendService.getEachId(fromId, toId);
		FriendChatRecord friendChatRecord = friendRepository.findById(eachId).get();

		StringBuilder sb = new StringBuilder();
		for (Chitchat chitchat : friendChatRecord.getRecord()) {
			sb.append(chitchat.getUserId()).append("(").append(chitchat.getName()).append(")-")
					.append(chitchat.getDate()).append("-").append(chitchat.getChat()).append("<br/>");
		}

		return sb.toString();
	}

	/**
	 * 获取群聊聊天记录(下载)
	 * @param message
	 * @return
	 */
	@Override
	public String getGroupRecord(WebSocketMessage message) {
		String groupId = message.getFromId();
		GroupChatRecord groupChatRecord = groupRepository.findById(groupId).get();

		StringBuilder sb = new StringBuilder();
		for (Chitchat chitchat : groupChatRecord.getRecord()) {
			sb.append(chitchat.getUserId()).append("(").append(chitchat.getName()).append(")-")
					.append(chitchat.getDate()).append("-").append(chitchat.getChat()).append("<br/>");
		}

		return sb.toString();
	}

	/**
	 * 获取好友聊天记录
	 * @param message
	 * @return
	 */
	@Override
	public String getNumberOfFriendRecord(WebSocketMessage message) {
		String fromId = message.getFromId();
		String toId = message.getToId();
		int pageStart = message.getNumber();

		String eachId = friendService.getEachId(fromId, toId);

		int allSize = friendMapping.getSizeOfRecord(eachId);
		int start = Math.max(allSize - pageStart - 10, 0);
		int number = Math.min(allSize - pageStart, 10);

		List<Chitchat> record;
		try {
			record = friendMapping.getRecord(eachId, start, number).getRecord();
		} catch (Exception e) {
			record = new ArrayList<>();
		}

		ReplayChat replay = new ReplayChat();
		replay.setChatRecords(record);

		clearFriendRedis(message);
		return JSON.toJSONString(replay);
	}

	/**
	 * 获取群聊聊天记录
	 * @param message
	 * @return
	 */
	@Override
	public String getNumberOfGroupRecord(WebSocketMessage message) {
		String groupId = message.getToId();
		int pageStart = message.getNumber();

		int allSize = groupMapping.getSizeOfRecord(groupId);
		int start = Math.max(allSize - pageStart - 10, 0);
		int number = Math.min(allSize - pageStart, 10);

		List<Chitchat> record;
		try {
			record = groupMapping.getRecord(groupId, start, number).getRecord();
		} catch (Exception e) {
			record = new ArrayList<>();
		}

		ReplayChat replay = new ReplayChat();
		replay.setChatRecords(record);

		clearGroupRedis(message);
		return JSON.toJSONString(replay);
	}
}
