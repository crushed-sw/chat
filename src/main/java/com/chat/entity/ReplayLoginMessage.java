package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayLoginMessage {
	public static final ReplayLoginMessage USER_ID_NOT_EXIT = new ReplayLoginMessage(false, 1);
	public static final ReplayLoginMessage USER_PASSWORD_WORING = new ReplayLoginMessage(false, 2);
	public static final ReplayLoginMessage USER_ID_OR_PASSWORD_NULL = new ReplayLoginMessage(false, 3);

	private static final ReplayLoginMessage SUCCEED_LOGIN = new ReplayLoginMessage(true, 0);

	private boolean succeed;
	private Integer errStatus;
	private String token;
	private String userId;
	private String userName;

	public ReplayLoginMessage(boolean succeed, Integer errStatus) {
		this(succeed, errStatus, "", "", "");
	}

	public static ReplayLoginMessage setSucceedLogin(String token, String userId, String userName) {
		ReplayLoginMessage.SUCCEED_LOGIN.setToken(token);
		ReplayLoginMessage.SUCCEED_LOGIN.setUserId(userId);
		ReplayLoginMessage.SUCCEED_LOGIN.setUserName(userName);
		return ReplayLoginMessage.SUCCEED_LOGIN;
	}
}
