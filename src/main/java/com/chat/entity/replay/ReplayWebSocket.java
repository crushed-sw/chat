package com.chat.entity.replay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * websocket主动通知的JSON实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplayWebSocket {
	private Integer status;
	private String id;
	private String avatar;
	private String name;
	private String message;
	private String date;

	public static final int SEND_TO_USER = 1;
	public static final int SEND_TO_GROUP = 2;
	public static final int UPDATE_FRIEND = 3;
	public static final int UPDATE_GROUP = 4;
	public static final int UPDATE_NOTICE = 5;
}
