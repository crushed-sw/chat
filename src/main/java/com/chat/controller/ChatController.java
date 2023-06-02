package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.util.CommondUtil;
import com.chat.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chat/refresh")
public class ChatController {
	@Autowired
	RedisUtil redisUtil;

	/**
	 * 每次刷新请求
	 * @return 返回未读消息
	 */
	@UserLoginToken
	@GetMapping
	public String doFresh(HttpServletRequest req) {
		String userId = (String) req.getAttribute("userId");
		Map<Object, Object> hashEntries = redisUtil.getHashEntries("chat-" + userId);
		return CommondUtil.mapToJson(hashEntries);
	}
}
