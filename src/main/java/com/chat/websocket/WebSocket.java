package com.chat.websocket;

import com.alibaba.fastjson.JSON;
import com.chat.entity.Friend;
import com.chat.entity.GroupChatRecord;
import com.chat.entity.User;
import com.chat.entity.UserInGroup;
import com.chat.entity.replay.ReplayWebSocket;
import com.chat.mapper.FriendRepository;
import com.chat.mapper.GroupRepository;
import com.chat.service.UserService;
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
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/chat/ws/{userId}")
public class WebSocket {
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	FriendRepository friendRepository;

	@Autowired
	UserService userService;

	@Autowired
	RedisUtil redisUtil;

	private Session session;
	private String userId;

	private static ConcurrentHashMap<String, WebSocket> webSockets = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

	/**
	 * 连接websocket时初始化和添加至session池里
	 * @param session
	 * @param userId
	 */
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

	/**
	 * websocket 关闭时处理
	 */
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

	}

	/**
	 * 错误时异常处理
	 * @param session
	 * @param error
	 */
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
	public void sendGroupMessage(String groupId, String sender, ReplayWebSocket replay) {
		String groupKey = "group-" + groupId;

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
					log.info("[websocket] " + sender + " 发送消息");
					sessionObject.getAsyncRemote().sendText(JSON.toJSONString(replay));
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
			User user = userService.getUserById(friendId);
			Friend friend = new Friend(user.getAvatar(), friendId, user.getUserName(),
					"", 1);
			map.put(keyFriend, JSON.toJSONString(friend));
			redisUtil.add(key, map);
		}

		if(sessionObject != null && sessionObject.isOpen()) {
			try {
				log.info("[websocket] " + userId + " 发送消息");
				sessionObject.getAsyncRemote().sendText(JSON.toJSONString(replay));
			} catch (Exception e) {
				log.error("[websocket] 发送错误");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送提示消息
	 * @param userId
	 * @param replay
	 */
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
				sessionObject.getAsyncRemote().sendText(JSON.toJSONString(replay));
			} catch (Exception e) {
				log.error("[websocket] 发送错误");
				e.printStackTrace();
			}
		}
	}
}
