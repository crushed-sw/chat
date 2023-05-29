package com.chat.controller;

import com.chat.entity.MessageJson;
import com.chat.entity.ReplayLoginMessage;
import com.chat.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/login")
@RestController
@Slf4j
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
		log.info("[login] " + messageJson.getUserId() + " 登陆");
		return loginService.getReplayLoginMessage(messageJson.getUserId(), messageJson.getPassword());
	}

}
