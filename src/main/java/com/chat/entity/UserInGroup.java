package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户里显示群聊的结构类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInGroup {
	private String name;
	private String avatar;
	private String groupId;
}
