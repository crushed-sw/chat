package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.FriendMessageJson;
import com.chat.entity.ReplayFriend;
import com.chat.service.FriendService;
import com.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chat/friendGroup")
public class FriendGroupController {
	@Autowired
	FriendService friendService;

	@UserLoginToken
	@PostMapping
	public ReplayFriend insertFriendGroup(@RequestBody FriendMessageJson messageJson) {
		String userId = messageJson.getUserId();
		String groupName = messageJson.getGroupName();
		log.info("[servlet] " + userId + " 新建分组 " + groupName);
		friendService.insertFriendGroup(userId, groupName);
		return friendService.getReplayFriend(userId);
	}

	@UserLoginToken
	@DeleteMapping
	public ReplayFriend deleteFriendGroup(@RequestBody FriendMessageJson messageJson) {
		String userId = messageJson.getUserId();
		String groupName = messageJson.getGroupName();
		log.info("[servlet] " + userId + " 删除分组 " + groupName);
		friendService.deleteFriendGroup(userId, groupName);
		return friendService.getReplayFriend(userId);
	}

	@UserLoginToken
	@PutMapping
	public ReplayFriend updateFriendGroup(@RequestBody FriendMessageJson messageJson) {
		String userId = messageJson.getUserId();
		String groupName = messageJson.getGroupName();
		String newGroupName = messageJson.getNewGroupName();
		log.info("[servlet] " + userId + " 修改分组 " + groupName + " 为 " + newGroupName);
		return friendService.getReplayFriend(userId);
	}
}