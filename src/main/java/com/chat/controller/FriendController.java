package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.recive.FriendMessageJson;
import com.chat.entity.replay.ReplayFriend;
import com.chat.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 好友界面的请求
 */
@Slf4j
@RestController
@RequestMapping("/chat/friend")
public class FriendController {
	@Autowired
	FriendService friendService;

	/**
	 * 请求好友列表
	 * @return 返回好友列表信息
	 */
	@UserLoginToken
	@GetMapping
	public ReplayFriend getFriends(HttpServletRequest req) {
		String userId = (String) req.getAttribute("userId");
		log.info("[servlet] " + userId + " 获取好友列表");
		return friendService.getReplayFriend(userId);
	}

	/**
	 * 添加好友
	 * @param message 前端发送的JSON
	 * @return 返回更新过的好友列表
	 */
	@UserLoginToken
	@PostMapping
	public ReplayFriend insertFriend(@RequestBody FriendMessageJson message) {
		String userId = message.getUserId();
		String friendId = message.getFriendId();
		String groupName = message.getGroupName();

		ReplayFriend replay = friendService.getReplayFriend(userId);
		if (userId.equals(friendId)) {
			replay.setStatus(ReplayFriend.ADD_SELF);
			log.info("[servlet] " + userId + " 添加自己为好友 " + friendId);
		} else if(friendService.isFriend(userId, friendId)) {
			replay.setStatus(ReplayFriend.ALREADY_ADDED);
			log.info("[servlet] " + userId + " 添加已添加好友 " + friendId);
		} else if(!friendService.addFriend(userId, friendId, groupName)) {
			replay.setStatus(ReplayFriend.FRIEND_ID_NOT_EXIT);
			log.info("[servlet] " + userId + " 添加好友 " + friendId + " 不存在");
		} else {
			log.info("[servlet] " + userId + " 添加好友 " + friendId);
		}
		return replay;
	}

	/**
	 * 删除好友
	 * @param message 需要删除的信息JSON
	 * @return 返回更新过的好友列表
	 */
	@UserLoginToken
	@DeleteMapping
	public ReplayFriend deleteFriend(@RequestBody FriendMessageJson message) {
		log.info("[servlet] " + message.getUserId() + " 删除好友 " + message.getFriendId());
		friendService.deleteFriendById(message.getUserId(), message.getGroupName(), message.getFriendId());
		return friendService.getReplayFriend(message.getUserId());
	}

	/**
	 * 好友移动分组
	 * @param messageJson 需要移动的信息JSON
	 * @return 返回更新过的好友列表
	 */
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
