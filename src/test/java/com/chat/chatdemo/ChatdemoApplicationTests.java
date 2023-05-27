package com.chat.chatdemo;

import com.chat.entity.User;
import com.chat.mapper.UserMapping;
import com.chat.service.UserService;
import com.chat.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatdemoApplicationTests {
	@Autowired
	UserService userService;

	@Autowired
	UserMapping userMapping;

	@Autowired
	RedisUtil redisUtil;

	@Test
	void getRedis() {
		System.out.println(redisUtil.getMapString("test", "123"));
	}

	@Test
	void setRedis() {
		Map<String, String> map = new HashMap<>();
		map.put("123", "456");
		redisUtil.add("test", map);
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
		userMapping.appendGroup("7777", "88888");
	}

	@Test
	void insertFriendGroup() {
		userMapping.appendFriendGroup("7777", "同学");
	}

	@Test
	void insertFriend() {
		userMapping.appendFriend("7777", "同学", "789456", "guess", "77777789");
	}

}
