package com.chat.service.impl;

import com.chat.entity.replay.ReplayLoginMessage;
import com.chat.entity.User;
import com.chat.service.LoginService;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import com.chat.util.JwtUtil;
import com.chat.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	UserService userService;

	@Autowired
	RedisUtil redisUtil;

	public ReplayLoginMessage getReplayLoginMessage(String userId, String password) {
		return judgeNotNll(userId, password);
	}

	private ReplayLoginMessage judgeNotNll(String userId, String password) {
		if(CommondUtil.judgeStringEmpty(userId) || CommondUtil.judgeStringEmpty(password)) {
			return ReplayLoginMessage.USER_ID_OR_PASSWORD_NULL;
		}
		return judgeExist(userId, password);
	}

	private ReplayLoginMessage judgeExist(String userId, String password) {
		User user = userService.getUserById(userId);
		if(user == null) {
			return ReplayLoginMessage.USER_ID_NOT_EXIT;
		}
		return judgePassword(userId, password, user);
	}

	private ReplayLoginMessage judgePassword(String userId, String password, User user) {
		if(!Objects.equals(user.getPassword(), password)) {
			return ReplayLoginMessage.USER_PASSWORD_WORING;
		}

		String token = JwtUtil.createJWT(userId);
		if(redisUtil.hashKey("token", userId)) {
			redisUtil.delete("token", userId);
		}
		Map<String, String> map = new HashMap<>();
		map.put(userId, token);
		redisUtil.add("token", map);

		String userName = user.getUserName();
		if(CommondUtil.judgeStringEmpty(userName)) {
			userName = userId;
		}

		return ReplayLoginMessage.setSucceedLogin(token, userId, userName, user.getAvatar());
	}
}
