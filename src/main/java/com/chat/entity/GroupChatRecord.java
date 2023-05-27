package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "group")
public class GroupChatRecord {
	@Id
	private String groupId;
	private String owner;
	private List<String> crew;
	private List<Chitchat> record;
}
