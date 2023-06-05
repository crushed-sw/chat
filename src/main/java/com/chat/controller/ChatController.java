package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.recive.WebSocketMessage;
import com.chat.service.ChatService;
import com.chat.util.CommondUtil;
import com.chat.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * 聊天界面的请求
 */
@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	ChatService chatService;

	/**
	 * 每次刷新请求
	 * @return 返回未读消息
	 */
	@UserLoginToken
	@GetMapping("/refresh")
	public String doFresh(HttpServletRequest req) {
		String userId = (String) req.getAttribute("userId");
		Map<Object, Object> hashEntries = redisUtil.getHashEntries("chat-" + userId);
		log.info("[servlet] " + userId + " 请求未读信息");
		return CommondUtil.mapToJson(hashEntries);
	}

	/**
	 * 聊天界面的请求
	 * @param message 前端请求JSON
	 * @return 返回的聊天消息
	 */
	@UserLoginToken
	@PostMapping
	public String chatEachOther(@RequestBody WebSocketMessage message) {
		Integer status = message.getStatus();
		String userId = message.getFromId();
		String toId = message.getToId();

		switch (status) {
			case WebSocketMessage.SEND_TO_USER -> {
				chatService.sendToUser(message);
				log.info("[servlet] " + userId + " 发送给好友 " + toId + " 信息");
			}
			case WebSocketMessage.SEND_TO_GROUP -> {
				chatService.sendToGroup(message);
				log.info("[servlet] " + userId + " 发送给群聊 " + toId + " 信息");
			}
			case WebSocketMessage.GET_FRIEND_RECORD -> {
				log.info("[servlet] " + userId + " 获取与好友 " + toId + " 聊天记录");
				return chatService.getNumberOfFriendRecord(message);
			}
			case WebSocketMessage.GET_GROUP_RECORD -> {
				log.info("[servlet] " + userId + " 获取与群聊 " + toId + " 聊天记录");
				return chatService.getNumberOfGroupRecord(message);
			}
		}
		return "{}";
	}

	/**
	 * 用于下载聊天记录
	 * @param message 前端请求JSON
	 * @return 返回的聊天记录文件
	 */
	@UserLoginToken
	@PostMapping("/download")
	public ResponseEntity<ByteArrayResource> chatDownload(@RequestBody WebSocketMessage message) throws Exception {
		String userId = message.getFromId();
		String toId = message.getToId();
		String content = "";

		if(message.getStatus().equals(WebSocketMessage.GET_FRIEND_RECORD)) {
			content = chatService.getFriendRecord(message);
			log.info("[servlet] " + userId + " 下载与好友 " + toId + " 聊天记录");
		} else if (message.getStatus().equals(WebSocketMessage.GET_GROUP_RECORD)) {
			content = chatService.getGroupRecord(message);
			log.info("[servlet] " + userId + " 下载与群聊 " + toId + " 聊天记录");
		}

		Path tempFile = Files.createTempFile(userId + "-chat", ".txt");
		Files.write(tempFile, content.getBytes());
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.TEXT_PLAIN);
		headers.setContentDispositionFormData("attachment", tempFile.getFileName().toString());
		headers.setContentLength(Files.size(tempFile));
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(tempFile));
		Files.delete(tempFile);

		return ResponseEntity.ok().headers(headers).body(resource);
	}
}
