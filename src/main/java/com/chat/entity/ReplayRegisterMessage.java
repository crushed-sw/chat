package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplayRegisterMessage {
	public static final Integer USER_ID_EXIT = 1;
	public static final Integer USER_ID_OR_PASSWORD_NULL = 2;

	private boolean succeed;
	private Integer state;
	private String userId;
	private String password;

	public ReplayRegisterMessage(boolean succeed, Integer state) {
		this(succeed, state, "", "");
	}
}
