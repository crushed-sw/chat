package com.chat.websocket;

import com.alibaba.fastjson.JSON;
import com.chat.entity.*;
import com.chat.entity.recive.WebSocketMessage;
import com.chat.entity.replay.ReplayGroup;
import com.chat.entity.replay.ReplayWebSocket;
import com.chat.mapper.FriendRepository;
import com.chat.mapper.GroupRepository;
import com.chat.mapper.UserRepository;
import com.chat.service.GroupService;
import com.chat.util.RedisUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/chat/ws/{userId}")
public class WebSocket {
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	FriendRepository friendRepository;
	@Autowired
	UserRepository userRepository;

	private Session session;
	private String userId;

	private static ConcurrentHashMap<String, WebSocket> webSockets = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session, @PathParam("userId") String userId) {
		try {
			this.session = session;
			this.userId = userId;
			if(webSockets.containsKey(userId)) {
				sessionPool.get(userId).close();

				webSockets.remove(this.userId);
				sessionPool.remove(this.userId);
			}
			webSockets.put(userId, this);
			sessionPool.put(userId, session);
			log.info("[websocket] 有新的连接 " + userId + " 总数为：" + webSockets.size());
		} catch (Exception e) {
			log.error("[websocket] " + userId + " 连接错误");
			e.printStackTrace();
		}
	}

	@OnClose
	public void OnClose() {
		try {
			webSockets.remove(this.userId);
			sessionPool.remove(this.userId);
			log.info("[websocket] " + userId + " 连接断开 总数为：" + webSockets.size());
		} catch (Exception e) {
			log.error("[websocket] " + userId + " 断开失败");
			e.printStackTrace();
		}
	}

	@OnMessage
	public void OnMessage(String message) {
		WebSocketMessage webSocketMessage = JSON.parseObject(message, WebSocketMessage.class);
		Integer status = webSocketMessage.getStatus();
		String fromId = webSocketMessage.getFromId();
		String toId = webSocketMessage.getToId();
		String chat = webSocketMessage.getMessage();

		if(status.equals(WebSocketMessage.SEND_TO_USER)) {
			User user = userRepository.findById(fromId).get();
			ReplayWebSocket replay = new ReplayWebSocket(ReplayWebSocket.SEND_TO_USER,
					user.getUserId(), user.getAvatar(), user.getUserName(), chat);
			sendUserMessage(toId, replay);
		} else if(status.equals(WebSocketMessage.SEND_TO_GROUP)) {
			GroupChatRecord group = groupRepository.findById(fromId).get();
			ReplayWebSocket replay = new ReplayWebSocket(ReplayWebSocket.SEND_TO_GROUP,
					group.getGroupId(), group.getAvatar(), group.getName(), chat);
			sendUserMessage(toId, replay);
		} else {

		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		log.error("[websocket] " + userId + " 错误, 原因：" + error.getMessage());
		error.printStackTrace();
	}

	/**
	 * replay.id 发给 groupId
	 * @param groupId
	 * @param replay
	 */
	public void sendGroupMessage(String groupId, ReplayWebSocket replay) {
		String groupKey = "group-" + groupId;
		String sender = replay.getId();
		GroupChatRecord group = groupRepository.findById(groupId).get();

		List<String> crews = group.getCrew();
		crews.add(group.getOwner());
		for (String crew : crews) {
			if(crew.equals(sender)) {
				continue;
			}
			String key = "chat-" + crew;
			Session sessionObject = sessionPool.get(crew);

			Map<String, String> map = new HashMap<>();
			if(redisUtil.hashKey(key, groupKey)) {
				UserInGroup userInGroup = JSON.parseObject(redisUtil.getMapString(key, groupKey),
						UserInGroup.class);
				userInGroup.setNumber(userInGroup.getNumber() + 1);

				map.put(groupKey, JSON.toJSONString(userInGroup));
				redisUtil.add(key, map);
			} else {
				UserInGroup userInGroup = new UserInGroup(group.getName(), group.getAvatar(),
						group.getGroupId(), group.getOwner(), 1);
				map.put(groupKey, JSON.toJSONString(userInGroup));
				redisUtil.add(key, map);
			}

			if(sessionObject != null && sessionObject.isOpen()) {
				try {
					log.info("[websocket] " + userId + " 发送消息");
					sessionObject.getAsyncRemote().sendText(replay.getJson());
				} catch (Exception e) {
					log.error("[websocket] 发送错误");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * replay.id 发给 userId
	 * @param userId
	 * @param replay
	 */
	public void sendUserMessage(String userId, ReplayWebSocket replay) {
		String friendId = replay.getId();
		String key = "chat-" + userId;
		String keyFriend = "friend-" + friendId;
		Session sessionObject = sessionPool.get(userId);

		Map<String, String> map = new HashMap<>();
		log.info("[websocket] " + userId + " 消息进行缓存");
		if(redisUtil.hashKey(key, keyFriend)) {
			Friend friend = JSON.parseObject(redisUtil.getMapString(key, keyFriend), Friend.class);
			friend.setNumber(friend.getNumber() + 1);

			map.put(keyFriend, JSON.toJSONString(friend));
			redisUtil.add(key, map);
		} else {
			User user = userRepository.findById(friendId).get();
			Friend friend = new Friend(user.getAvatar(), friendId, user.getUserName(),
					"", 1);
			map.put(keyFriend, JSON.toJSONString(friend));
			redisUtil.add(key, map);
		}

		if(sessionObject != null && sessionObject.isOpen()) {
			try {
				log.info("[websocket] " + userId + " 发送消息");
				sessionObject.getAsyncRemote().sendText(replay.getJson());
			} catch (Exception e) {
				log.error("[websocket] 发送错误");
				e.printStackTrace();
			}
		}
	}

	public void sendRemind(String userId, ReplayWebSocket replay) {
		String key = "chat-" + userId;
		Session sessionObject = sessionPool.get(userId);

		Map<String, String> map = new HashMap<>();
		log.info("[websocket] " + userId + " 提示进行缓存");
		if(redisUtil.hashKey(key, "notice")) {
			int notice = Integer.parseInt(redisUtil.getMapString(key, "notice"));
			notice++;
			redisUtil.delete(key, "notice");
			map.put("notice", Integer.toString(notice));
			redisUtil.add(key, map);
		} else {
			map.put("notice", "1");
			redisUtil.add(key, map);
		}

		if(sessionObject != null && sessionObject.isOpen()) {
			try {
				log.info("[websocket] " + userId + " 发送提示");
				sessionObject.getAsyncRemote().sendText(replay.getJson());
			} catch (Exception e) {
				log.error("[websocket] 发送错误");
				e.printStackTrace();
			}
		}
	}
}
