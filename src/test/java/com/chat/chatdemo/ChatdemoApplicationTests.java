package com.chat.chatdemo;

import com.chat.entity.GroupChatRecord;
import com.chat.entity.User;
import com.chat.mapper.*;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import com.chat.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatdemoApplicationTests {
	@Autowired
	UserService userService;
	@Autowired
	UserMapping userMapping;
	@Autowired
	FriendRepository friendRepository;
	@Autowired
	FriendMapping friendMapping;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	GroupMapping groupMapping;

	@Autowired
	RedisUtil redisUtil;

	@Test
	void getRedis() {
		System.out.println((String) redisUtil.get("friendEachId"));
	}

	@Test
	void setRedis() {
		redisUtil.set("friendEachId", "1");
	}

	@Test
	void insertUser() {
		userService.insertUser("7777", "crushed", "789");
	}

	@Test
	void queryUser() {
		User user = userService.getUserById("7777");
		System.out.println(user);
	}

	@Test
	void updateUser() {
		User user = new User("crushed", "111111", "222222");
		userService.updateUserById(user);
	}

	@Test
	void deleteUser() {
		userService.deleteById("7777");
	}

	@Test
	void insertGroup() {
		userMapping.appendGroup("101", "88888");
	}

	@Test
	void insertFriendGroup() {
		userMapping.appendFriendGroup("102", "家人");
		userMapping.appendFriendGroup("102", "同学");
	}

	@Test
	void insertFriend() {
		userMapping.appendFriend("102", "家人", "10115", "10115", "10115", CommondUtil.getAvatar());
		userMapping.appendFriend("102", "家人", "10116", "10116", "10116", CommondUtil.getAvatar());
		userMapping.appendFriend("102", "家人", "10117", "10117", "10117", CommondUtil.getAvatar());
		userMapping.appendFriend("102", "家人", "10118", "10118", "10118", CommondUtil.getAvatar());
		userMapping.appendFriend("102", "同学", "10008", "10008", "10008", CommondUtil.getAvatar());
		userMapping.appendFriend("102", "同学", "10008", "10008", "10008", CommondUtil.getAvatar());
		userMapping.appendFriend("102", "同学", "10008", "10008", "10008", CommondUtil.getAvatar());
		userMapping.appendFriend("102", "同学", "10008", "10008", "10008", CommondUtil.getAvatar());
	}

	@Test
	void deleteFriend() {
		userMapping.deleteFriend("101", "同学", "12345");
		userMapping.deleteFriend("101", "同学", "10005");
		userMapping.deleteFriend("101", "同学", "10006");
		userMapping.deleteFriend("101", "同学", "10007");
		userMapping.deleteFriend("101", "同学", "10008");
		userMapping.deleteFriend("101", "家人", "10115");
		userMapping.deleteFriend("101", "家人", "10116");
		userMapping.deleteFriend("101", "家人", "10117");
		userMapping.deleteFriend("101", "家人", "10118");
	}

	@Test
	void deleteUserGroup() {
		userMapping.deleteGroup("101", "00000");
	}

	@Test
	void updateUserGroup() {
		userMapping.updateFriendGroup("101", "同学", "朋友");
	}

	@Test
	void insertGroupRecord() {
		groupRepository.save(new GroupChatRecord("111", "222", "", "", new ArrayList<>(), new ArrayList<>()));
	}

	@Test
	void insertGroupCrew() {
		groupMapping.appendCrew("111", "777");
		groupMapping.appendCrew("111", "888");
	}

	@Test
	void deleteGroupCrew() {
		groupMapping.deleteCrew("111", "777");
	}

	@Test
	void deleteGroup() {
		groupRepository.deleteById("111");
	}

	@Test
	void renameGroup() {
		groupMapping.updateName("111", "haha");
	}
}
