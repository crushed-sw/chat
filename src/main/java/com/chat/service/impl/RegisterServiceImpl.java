package com.chat.service.impl;

import com.chat.entity.ReplayRegisterMessage;
import com.chat.entity.User;
import com.chat.service.RegisterService;
import com.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	UserService userService;

	@Override
	public ReplayRegisterMessage getREgisterMessage(String userId, String password, String userName) {
		return judgeNotNull(userId, password, userName);
	}

	private ReplayRegisterMessage judgeNotNull(String userId, String password, String userName) {
		if(userId == null || userId.trim().equals("")
		|| password == null || password.trim().equals("")) {
			return new ReplayRegisterMessage(false, ReplayRegisterMessage.USER_ID_OR_PASSWORD_NULL);
		}
		return judgeExist(userId, password, userName);
	}

	private ReplayRegisterMessage judgeExist(String userId, String password, String userName) {
		User user = userService.getUserById(userId);
		if(user != null) {
			return new ReplayRegisterMessage(false, ReplayRegisterMessage.USER_ID_EXIT);
		}
		return judgeNameIsDefault(userId, password, userName);
	}

	private ReplayRegisterMessage judgeNameIsDefault(String userId, String password, String userName) {
		if(userName == null || userName.trim().equals("")) {
			userName = userId;
		}
		userService.insertUser(userId, userName, password);
		return new ReplayRegisterMessage(true, 0, userId, password);
	}
}
