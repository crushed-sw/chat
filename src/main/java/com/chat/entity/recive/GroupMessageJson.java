package com.chat.entity.recive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageJson {
	private String userId;
	private String groupId;
	private String groupName;
}
