package com.chat.controller;

import com.chat.entity.MessageJson;
import com.chat.entity.ReplayRegisterMessage;
import com.chat.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@Slf4j
public class RegisterController {
	@Autowired
	RegisterService registerService;

	/**
	 * 注册反馈
	 * @param messageJson
	 * @return
	 */
	@PostMapping
	public ReplayRegisterMessage register(@RequestBody MessageJson messageJson) {
		log.info("[register] " + messageJson.getUserId() + " 注册");
		return registerService.getRegisterMessage(messageJson.getUserId(),
				messageJson.getPassword(), messageJson.getUserName());
	}
}
