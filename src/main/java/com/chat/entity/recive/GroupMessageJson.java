package com.chat.entity.recive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群聊请求的JSON实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageJson {
	private String userId;
	private String groupId;
	private String groupName;
}
