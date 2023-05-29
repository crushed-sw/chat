package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 反馈friend请求类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayFriend {
	private List<UserInFriend> friendList;
}
