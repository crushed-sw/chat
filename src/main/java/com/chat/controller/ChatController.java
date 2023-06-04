package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.recive.WebSocketMessage;
import com.chat.service.ChatService;
import com.chat.util.CommondUtil;
import com.chat.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

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
		return CommondUtil.mapToJson(hashEntries);
	}

	@UserLoginToken
	@PostMapping
	public String chatEachOther(@RequestBody WebSocketMessage message) {
		Integer status = message.getStatus();

		System.out.println(message);

		switch (status) {
			case WebSocketMessage.SEND_TO_USER ->
				chatService.sendToUser(message);
			case WebSocketMessage.SEND_TO_GROUP ->
				chatService.sendToGroup(message);
			case WebSocketMessage.GET_FRIEND_RECORD -> {
				return chatService.getNumberOfFriendRecord(message);
			}
			case WebSocketMessage.GET_GROUP_RECORD -> {
				return chatService.getNumberOfGroupRecord(message);
			}
		}
		return "{}";
	}

	@UserLoginToken
	@PostMapping("/download")
	public ResponseEntity<ByteArrayResource> chatDownload(@RequestBody WebSocketMessage message) throws Exception {
		String fromId = message.getFromId();
		String content = "";

		if(message.getStatus().equals(WebSocketMessage.SEND_TO_USER)) {
			content = chatService.getFriendRecord(message);
		} else if (message.getStatus().equals(WebSocketMessage.SEND_TO_GROUP)) {
			content = chatService.getGroupRecord(message);
		}

		Path tempFile = Files.createTempFile(fromId + "-chat", ".txt");
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
