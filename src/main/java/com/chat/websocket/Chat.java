package com.chat.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/chat/ws/{userId}")
public class Chat  {

	private Session session;
	private String userId;

	private static ConcurrentHashMap<String, Chat> webSockets = new ConcurrentHashMap<>();
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

	}

	@OnError
	public void onError(Session session, Throwable error) {
		log.error("[websocket] " + userId + " 错误, 原因：" + error.getMessage());
		error.printStackTrace();
	}

	public void sendOneMessage(String friendId, String message) {
		sendMessage(friendId, message, "单点消息");
	}

	public void sendMoreMessage(List<String> friendsId, String message) {
		for(String friendId : friendsId) {
			sendMessage(friendId, message, "群消息");
		}
	}

	private void sendMessage(String friendId, String message, String type) {
		Session session = sessionPool.get(friendId);
		if(session != null && session.isOpen()) {
			try {
				log.info("[websocket] 发送给用户 " + friendId + " " + type + "：" + message);
				session.getAsyncRemote().sendText(message);
			} catch (Exception e) {
				log.error("[websocket] 发送错误");
				e.printStackTrace();
			}
		}
	}
}
