package com.chat.service.impl;

import com.chat.entity.ReplayLoginMessage;
import com.chat.entity.User;
import com.chat.service.LoginService;
import com.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	UserService userService;

	public ReplayLoginMessage getReplayLoginMessage(String userId, String password) {
		return judgeNotNll(userId, password);
	}

	private ReplayLoginMessage judgeNotNll(String userId, String password) {
		if(userId == null || userId.trim().equals("")
		|| password == null || password.trim().equals("")) {
			return new ReplayLoginMessage(false, ReplayLoginMessage.USER_ID_OR_PASSWORD_NULL);
		}
		return judgeExist(userId, password);
	}

	private ReplayLoginMessage judgeExist(String userId, String password) {
		User user = userService.getUserById(userId);
		if(user == null) {
			return new ReplayLoginMessage(false, ReplayLoginMessage.USER_ID_NOT_EXIT);
		}
		return judgePassword(userId, password, user);
	}

	private ReplayLoginMessage judgePassword(String userId, String password, User user) {
		if(!Objects.equals(user.getPassword(), password)) {
			return new ReplayLoginMessage(false, ReplayLoginMessage.USER_PASSWORD_WORING);
		}
		return new ReplayLoginMessage(true, 0, user.getUserId(), user.getUserName());
	}
}
