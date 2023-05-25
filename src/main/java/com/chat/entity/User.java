package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "user")
public class User {
	@Id
	private String userId;
	private String userName;
	private String password;
	private String headPicture;
	private Map<String, List<String>> friends;
	private List<String> groups;

	public User(String userId, String userName, String password) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.friends = new HashMap<>();
		this.groups = new ArrayList<>();
	}

	public User(String userId) {
		this(userId, "", "");
	}
}
