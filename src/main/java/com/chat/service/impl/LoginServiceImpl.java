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

/**
 * 登陆逻辑类
 */
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	UserService userService;

	@Autowired
	RedisUtil redisUtil;

	/**
	 * 获取登陆反馈信息
	 * @param userId
	 * @param password
	 * @return
	 */
	public ReplayLoginMessage getReplayLoginMessage(String userId, String password) {
		return judgeNotNll(userId, password);
	}

	/**
	 * 判断俩阐述是否为空
	 * @param userId
	 * @param password
	 * @return
	 */
	private ReplayLoginMessage judgeNotNll(String userId, String password) {
		if(CommondUtil.judgeStringEmpty(userId) || CommondUtil.judgeStringEmpty(password)) {
			return ReplayLoginMessage.USER_ID_OR_PASSWORD_NULL;
		}
		return judgeExist(userId, password);
	}

	/**
	 * 判断用户是否已注册
	 * @param userId
	 * @param password
	 * @return
	 */
	private ReplayLoginMessage judgeExist(String userId, String password) {
		User user = userService.getUserById(userId);
		if(user == null) {
			return ReplayLoginMessage.USER_ID_NOT_EXIT;
		}
		return judgePassword(userId, password, user);
	}

	/**
	 * 判断密码与ID是否匹配
	 * @param userId
	 * @param password
	 * @param user
	 * @return
	 */
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
