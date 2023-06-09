package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.recive.GroupMessageJson;
import com.chat.entity.replay.ReplayGroup;
import com.chat.service.GroupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 有关群组的请求
 */
@Slf4j
@RestController
@RequestMapping("/chat/group")
public class GroupController {
	@Autowired
	GroupService groupService;

	/**
	 * 获取群聊信息
	 * @return 返回所有群聊信息
	 */
	@UserLoginToken
	@GetMapping
	public ReplayGroup getGroups(HttpServletRequest req) {
		String userId = (String) req.getAttribute("userId");
		log.info("[servlet] " + userId + " 获取群组列表");
		return groupService.getReplayGroup(userId);
	}

	/**
	 * 添加群聊
	 * @param groupMessageJson 添加群聊的用户和群聊ID
	 * @return 返回更新后的群聊信息
	 */
	@UserLoginToken
	@PostMapping
	public ReplayGroup insertGroup(@RequestBody GroupMessageJson groupMessageJson) {
		String userId = groupMessageJson.getUserId();
		String groupId = groupMessageJson.getGroupId();

		ReplayGroup replay = groupService.getReplayGroup(userId);
		int value = groupService.isInGroup(userId, groupId);
		switch (value) {
			case -3 -> {
				replay.setStatus(ReplayGroup.GROUP_NOT_FOUND);
				log.info("[servlet] " + userId + " 添加群聊 " + groupId + " 不存在");
			}
			case -2 -> {
				replay.setStatus(ReplayGroup.ALREADY_ADDED);
				log.info("[servlet] " + userId + " 重复添加群聊 " + groupId);
			}
			case -1 -> {
				replay.setStatus(ReplayGroup.OWNER_ADD_SELF);
				log.info("[servlet] " + userId + " 添加自己创建的群聊 " + groupId);
			}
			case 0 -> {
				groupService.addGroup(userId, groupId);
				replay = groupService.getReplayGroup(userId);
				log.info("[servlet] " + userId + " 添加群聊 " + groupId);
			}
		}
		return replay;
	}

	/**
	 * 删除群聊
	 * @param groupMessageJson 删除群聊的用户和群聊ID
	 * @return 返回更新后的群聊信息
	 */
	@UserLoginToken
	@DeleteMapping
	public ReplayGroup deleteGroup(@RequestBody GroupMessageJson groupMessageJson) {
		String userId = groupMessageJson.getUserId();
		String groupId = groupMessageJson.getGroupId();
		log.info("[servlet] " + userId + " 删除群聊 " + groupId);
		groupService.deleteGroupById(userId, groupId);
		return groupService.getReplayGroup(userId);
	}

	/**
	 * 创建群聊
	 * @param groupMessageJson 创建群聊的用户和群聊名
	 * @return 返回更新后的群聊信息
	 */
	@UserLoginToken
	@PostMapping("/create")
	public ReplayGroup createGroup(@RequestBody GroupMessageJson groupMessageJson) {
		String userId = groupMessageJson.getUserId();
		String groupName = groupMessageJson.getGroupName();
		log.info("[servlet] " + userId + " 创建群组 " + groupName);
		groupService.createGroup(userId, groupName);
		return groupService.getReplayGroup(userId);
	}
}
