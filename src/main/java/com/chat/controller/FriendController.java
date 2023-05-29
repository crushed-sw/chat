package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.FriendMessageJson;
import com.chat.entity.ReplayFriend;
import com.chat.service.FriendService;
import com.chat.websocket.Chat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chat/friend")
public class FriendController {
	@Autowired
	FriendService friendService;
	@Autowired
	Chat chat;

	@UserLoginToken
	@GetMapping
	public ReplayFriend getFriends(HttpServletRequest req) {
		String userId = (String) req.getAttribute("userId");
		log.info("[servlet] " + userId + " 获取好友列表");
		return friendService.getReplayFriend(userId);
	}

	@UserLoginToken
	@DeleteMapping
	public ReplayFriend deleteFriend(@RequestBody FriendMessageJson message) {
		log.info("[servlet] " + message.getUserId() + " 删除好友 " + message.getFriendId());
		friendService.deleteFriendById(message.getUserId(), message.getGroupName(), message.getFriendId());
		return friendService.getReplayFriend(message.getUserId());
	}

	@UserLoginToken
	@PutMapping
	public ReplayFriend updateFriend(@RequestBody FriendMessageJson messageJson) {
		log.info("[servlet] " + messageJson.getUserId() + " 将好友 " + messageJson.getFriendId()
				+ " 从分组 " + messageJson.getGroupName() + " 移动到 " + messageJson.getNewGroupName());
		friendService.updateFriendById(messageJson.getUserId(), messageJson.getGroupName(),
				                       messageJson.getNewGroupName(), messageJson.getFriendId());
		return friendService.getReplayFriend(messageJson.getUserId());
	}
}
