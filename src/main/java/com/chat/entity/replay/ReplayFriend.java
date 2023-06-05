package com.chat.entity.replay;

import com.chat.entity.UserInFriend;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 反馈friend请求实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayFriend {
	private List<UserInFriend> friendList;
	private Integer status;

	public static final Integer GROUP_NAME_SAME = -1;
	public static final Integer FRIEND_ID_NOT_EXIT = -2;
	public static final Integer ALREADY_ADDED = -3;
	public static final Integer ADD_SELF = -4;
}
