package com.chat.service;

import com.chat.entity.User;

public interface UserService {
	boolean insertUser(String userId, String userName, String password);
	User getUserById(String userId);
	boolean deleteById(String userId);
	boolean updateUserById(User user);

}
