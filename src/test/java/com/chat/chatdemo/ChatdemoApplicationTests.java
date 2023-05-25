package com.chat.chatdemo;

import com.chat.entity.User;
import com.chat.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatdemoApplicationTests {
	@Autowired
	UserService userService;

	@Test
	void test() {
		System.out.println("hello");
	}

	@Test
	void insertUser() {
		userService.insertUser("123456", "crush", "789");
	}

	@Test
	void queryUser() {
		User user = userService.getUserById("123456");
		System.out.println(user);
	}

	@Test
	void updateUser() {
		User user = new User("crushed", "111111", "222222");
		userService.updateUserById(user);
	}

	@Test
	void deleteUser() {
		userService.deleteById("123456");
	}

}
