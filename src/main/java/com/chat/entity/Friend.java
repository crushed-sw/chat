package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 好友类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
	private String avatar;
	private String friendId;
	private String friendName;
	private String friendEachId;
	private Integer number;
}
