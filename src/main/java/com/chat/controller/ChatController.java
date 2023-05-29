package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/refresh")
public class ChatController {
	/**
	 * 每次刷新请求
	 * @return 返回未读消息
	 */
	@UserLoginToken
	@GetMapping
	public String doFresh() {
		return "{}";
	}
}
