package com.chat.entity.replay;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登陆注册反馈实体类
 */
@Data
@AllArgsConstructor
public class ReplayRegisterMessage {
	public static final ReplayRegisterMessage USER_ID_EXIT = new ReplayRegisterMessage(false, 1);
	public static final ReplayRegisterMessage USER_ID_OR_PASSWORD_NULL = new ReplayRegisterMessage(false, 2);

	private static ReplayRegisterMessage SUCCEED_REGISTER = new ReplayRegisterMessage(true, 0);

	private boolean succeed;
	private Integer state;
	private String userId;

	public ReplayRegisterMessage(boolean succeed, Integer state) {
		this(succeed, state, "");
	}

	public static ReplayRegisterMessage getSucceedRegister(String userId) {
		ReplayRegisterMessage.SUCCEED_REGISTER.setUserId(userId);
		return ReplayRegisterMessage.SUCCEED_REGISTER;
	}
}
