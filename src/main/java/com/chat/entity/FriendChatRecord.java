package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友聊天记录类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "friend")
public class FriendChatRecord {
	@Id
	private String friendId;
	private String userIdOne;
	private String userIdTwo;
	private List<Chitchat> record;

	public FriendChatRecord(String friendId, String userIdOne, String userIdTwo) {
		this.friendId = friendId;
		this.userIdOne = userIdOne;
		this.userIdTwo = userIdTwo;
		this.record = new ArrayList<>();
	}
}
