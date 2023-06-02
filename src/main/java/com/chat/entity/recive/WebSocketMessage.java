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
	private String message;

	public static final Integer SEND_TO_USER = 1;
	public static final Integer SEND_TO_GROUP = 2;
}
