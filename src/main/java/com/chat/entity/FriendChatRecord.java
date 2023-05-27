package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

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

	public FriendChatRecord(Long friendId, String userIdOne, String userIdTwo) {
		this.friendId = String.valueOf(friendId);
		this.userIdOne = userIdOne;
		this.userIdTwo = userIdTwo;
		this.record = new ArrayList<>();
	}
}
