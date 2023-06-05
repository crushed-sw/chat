package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.recive.FriendMessageJson;
import com.chat.entity.replay.ReplayFriend;
import com.chat.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 与好友分组相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/chat/friendGroup")
public class FriendGroupController {
	@Autowired
	FriendService friendService;

	/**
	 * 新增好友分组
	 * @param messageJson 需要新增好友的用户和分组信息
	 * @return 返回更新后的好友列表
	 */
	@UserLoginToken
	@PostMapping
	public ReplayFriend insertFriendGroup(@RequestBody FriendMessageJson messageJson) {
		String userId = messageJson.getUserId();
		String groupName = messageJson.getGroupName();
		log.info("[servlet] " + userId + " 新建分组 " + groupName);

		boolean flag = friendService.insertFriendGroup(userId, groupName);
		ReplayFriend replayFriend = friendService.getReplayFriend(userId);
		if(!flag) {
			replayFriend.setStatus(ReplayFriend.GROUP_NAME_SAME);
		}

		return replayFriend;
	}

	/**
	 * 删除好友分组
	 * @param messageJson 需要删除好友的用户和分组信息
	 * @return 返回更新后的好友列表
	 */
	@UserLoginToken
	@DeleteMapping
	public ReplayFriend deleteFriendGroup(@RequestBody FriendMessageJson messageJson) {
		String userId = messageJson.getUserId();
		String groupName = messageJson.getGroupName();
		log.info("[servlet] " + userId + " 删除分组 " + groupName);
		friendService.deleteFriendGroup(userId, groupName);
		return friendService.getReplayFriend(userId);
	}

	/**
	 * 修改好友分组名称
	 * @param messageJson 需要更改好友分组的用户和分组信息
	 * @return 返回更新后的好友列表
	 */
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
