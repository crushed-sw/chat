package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayLoginMessage {
	public static final Integer USER_ID_NOT_EXIT = 1;
	public static final Integer USER_PASSWORD_WORING = 2;
	public static final Integer USER_ID_OR_PASSWORD_NULL = 3;

	private boolean succeed;
	private Integer errStatus;
	private String userId;
	private String userName;

	public ReplayLoginMessage(boolean succeed, Integer errStatus) {
		this(succeed, errStatus, "", "");
	}
}
