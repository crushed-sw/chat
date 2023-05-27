package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.MessageJson;
import com.chat.entity.ReplayLoginMessage;
import com.chat.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/login")
@RestController
public class LoginController {
	@Autowired
	LoginService loginService;

	/**
	 * 登陆反馈
	 * @param messageJson
	 * @return
	 */
	@PostMapping
	public ReplayLoginMessage getUserById(@RequestBody MessageJson messageJson) {
		return loginService.getReplayLoginMessage(messageJson.getUserId(), messageJson.getPassword());
	}

}
