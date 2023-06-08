package com.chat.service.impl;

import com.chat.entity.Notice;
import com.chat.entity.User;
import com.chat.entity.replay.ReplayRegisterMessage;
import com.chat.mapper.NoticeRepository;
import com.chat.service.RegisterService;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 注册逻辑类
 */
@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	UserService userService;
	@Autowired
	NoticeRepository noticeRepository;

	/**
	 * 获取注册反馈信息
	 * @param userId
	 * @param password
	 * @param userName
	 * @return
	 */
	@Override
	public ReplayRegisterMessage getRegisterMessage(String userId, String password, String userName) {
		return judgeChinese(userId, password, userName);
	}

	/**
	 * 判断userId是否合法
	 * @param userId
	 * @param password
	 * @param userName
	 * @return
	 */
	private ReplayRegisterMessage judgeChinese(String userId, String password, String userName) {
		if(!userId.matches("^[a-zA-Z0-9]*$")) {
			return ReplayRegisterMessage.USER_ID_IS_CHINESE;
		}

		return judgeNotNull(userId, password, userName);
	}

	/**
	 * 判断参数是否为空
	 * @param userId
	 * @param password
	 * @param userName
	 * @return
	 */
	private ReplayRegisterMessage judgeNotNull(String userId, String password, String userName) {
		if(CommondUtil.judgeStringEmpty(userId) || CommondUtil.judgeStringEmpty(password)) {
			return ReplayRegisterMessage.USER_ID_OR_PASSWORD_NULL;
		}
		return judgeExist(userId, password, userName);
	}

	/**
	 * 判断用户是否已注册
	 * @param userId
	 * @param password
	 * @param userName
	 * @return
	 */
	private ReplayRegisterMessage judgeExist(String userId, String password, String userName) {
		User user = userService.getUserById(userId);
		if(user != null) {
			return ReplayRegisterMessage.USER_ID_EXIT;
		}
		return judgeNameIsDefault(userId, password, userName);
	}

	/**
	 * 判断用户的name是否为空
	 * @param userId
	 * @param password
	 * @param userName
	 * @return
	 */
	private ReplayRegisterMessage judgeNameIsDefault(String userId, String password, String userName) {
		if(CommondUtil.judgeStringEmpty(userName)) {
			userName = userId;
		}
		userService.insertUser(userId, userName, password);
		Notice notice = new Notice(userId, new ArrayList<>());
		noticeRepository.save(notice);
		return ReplayRegisterMessage.getSucceedRegister(userId);
	}
}
