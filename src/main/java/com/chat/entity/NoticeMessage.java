package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeMessage {
	private Integer status;
	private String fromId;
	private String avatar;
	private String name;
	private String message;
	private String groupName;
	private String groupId;

	public static final Integer MESSAGE_ADDED = 1;
	public static final Integer MESSAGE_REJECTED = 2;
	public static final Integer ADD_FRIEND = 3;
	public static final Integer ADD_GROUP = 4;
	public static final Integer MESSAGE_NOTICE = 5;

	public NoticeMessage(Integer status, String fromId, String avatar, String name, String message) {
		this.status = status;
		this.fromId = fromId;
		this.avatar = avatar;
		this.name = name;
		this.message = message;
	}
}
