package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.NoticeMessage;
import com.chat.entity.recive.NoticeMessageJson;
import com.chat.entity.replay.ReplayNotice;
import com.chat.service.NoticeService;
import com.chat.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chat/notice")
public class NoticeController {
	@Autowired
	NoticeService noticeService;

	@Autowired
	RedisUtil redisUtil;

	/**
	 * 获取Notice列表
	 * @param req 获取前端传入的UserId
	 * @return 返回Notice列表对象
	 */
	@UserLoginToken
	@GetMapping
	public ReplayNotice getNotice(HttpServletRequest req) {
		String userId = (String) req.getAttribute("userId");
		String key = "chat-" + userId;
		redisUtil.delete(key, "notice");
		log.info("[servlet] " + userId + " 获取通知");
		return noticeService.getReplayNotice(userId);
	}

	/**
	 *
	 * @param message 获取前端传入的Json
	 * @return 返回Notice列表对象
	 */
	@UserLoginToken
	@PostMapping
	public ReplayNotice updateNotice(@RequestBody NoticeMessageJson message) {
		Integer status = message.getStatus();
		String userId = message.getUserId();
		String oppoId = message.getOppositeId();

		System.out.println(message);

		switch (status) {
			case NoticeMessageJson.ADD_FRIEND ->
					noticeService.addFriend(userId, oppoId, NoticeMessage.ADD_FRIEND,
							message.getGroupName(), message.getOppositeGroupName());
			case NoticeMessageJson.REFUSE_FRIEND ->
					noticeService.refuseFriend(userId, oppoId, NoticeMessage.ADD_FRIEND);
			case NoticeMessageJson.ADD_GROUP ->
					noticeService.addGroup(userId, oppoId, NoticeMessage.ADD_GROUP);
			case NoticeMessageJson.REFUSE_GROUP ->
					noticeService.refuseGroup(userId, oppoId, NoticeMessage.ADD_GROUP);
		}
		return noticeService.getReplayNotice(userId);
	}
}