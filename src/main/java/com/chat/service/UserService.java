package com.chat.service;

import com.chat.entity.User;

public interface UserService {
	void insertUser(String userId, String userName, String password);
	User getUserById(String userId);
	void deleteById(String userId);
	void updateUserById(User user);

}
