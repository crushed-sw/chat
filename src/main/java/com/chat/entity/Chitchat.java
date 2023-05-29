package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 聊天记录类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chitchat {
	private Date date;
	private String from;
	private String chat;
}
