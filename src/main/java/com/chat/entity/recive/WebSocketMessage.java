package com.chat.entity.recive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {
	private Integer status;
	private String fromId;
	private String toId;
	private String date;
	private String message;
	private Integer number;

	public static final int SEND_TO_USER = 1;
	public static final int SEND_TO_GROUP = 2;
	public static final int GET_FRIEND_RECORD = 3;
	public static final int GET_GROUP_RECORD = 4;
}
