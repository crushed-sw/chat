package com.chat.entity.recive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeMessageJson {
	private Integer status;
	private String userId;
	private String oppositeId;
	private String groupName;
	private String oppositeGroupName;

	public static final int ADD_FRIEND = 1;
	public static final int REFUSE_FRIEND = 2;
	public static final int ADD_GROUP = 3;
	public static final int REFUSE_GROUP = 4;
}
