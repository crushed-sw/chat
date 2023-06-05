package com.chat.entity.replay;

import com.chat.entity.UserInGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 反馈group请求实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayGroup {
	private List<UserInGroup> groups;
	private Integer status;

	public static final Integer GROUP_NOT_FOUND = -1;
	public static final Integer ALREADY_ADDED = -2;
	public static final Integer OWNER_ADD_SELF = -3;
}
