package com.chat.controller;

import com.chat.entity.recive.MessageJson;
import com.chat.entity.replay.ReplayRegisterMessage;
import com.chat.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 与注册相关的请求
 */
@RestController
@RequestMapping("/register")
@Slf4j
public class RegisterController {
	@Autowired
	RegisterService registerService;

	/**
	 * 注册反馈
	 * @param messageJson 前端传入Json
	 * @return 返回Json对象
	 */
	@PostMapping
	public ReplayRegisterMessage register(@RequestBody MessageJson messageJson) {
		log.info("[register] " + messageJson.getUserId() + " 注册");
		return registerService.getRegisterMessage(messageJson.getUserId(),
				messageJson.getPassword(), messageJson.getUserName());
	}
}
