package com.chat.entity.recive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 好友请求的JSON实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendMessageJson {
	private String userId;
	private String friendId;
	private String groupName;
	private String newGroupName;
}
