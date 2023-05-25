package com.chat.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
	public static final Integer SEND_TO_FRIEND = 1;
	public static final Integer SEND_TO_GROUP = 2;
	public static final Integer SEND_TO_INFORM = 3;

	private Integer state;
	private String fromId;
	private String toId;
	private String message;
	private Date date;
}
