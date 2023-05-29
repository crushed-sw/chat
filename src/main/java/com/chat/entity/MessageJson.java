package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接受客户端json类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageJson {
	private String userId;
	private String password;
	private String userName;
}
