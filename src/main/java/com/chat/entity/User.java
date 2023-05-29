package com.chat.entity;

import com.chat.util.CommondUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 用户类
 */
@Data
@NoArgsConstructor
@Document(collection = "user")
public class User {
	@Id
	private String userId;
	private String userName;
	private String password;
	private String avatar;
	private List<UserInFriend> friendGroups;
	private List<String> groups;

	public User(String userId, String userName, String password) {
		Random random = new Random();
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.avatar = CommondUtil.picture.get(random.nextInt(30));
		this.friendGroups = new ArrayList<>();
		this.groups = new ArrayList<>();
		friendGroups.add(new UserInFriend("我的好友", new ArrayList<>()));
	}

	public User(String userId) {
		this(userId, "", "");
	}
}
