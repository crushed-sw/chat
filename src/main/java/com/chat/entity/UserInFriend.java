package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户里显示好友的结构类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInFriend {
	private String groupName;
	private List<Friend> friends;
}
