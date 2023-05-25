package com.chat.service.impl;

import com.chat.entity.User;
import com.chat.mapper.UserRepository;
import com.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repository;

	@Override
	public boolean insertUser(String userId, String userName, String password) {
		repository.save(new User(userId, userName, password));
		return true;
	}

	@Override
	public User getUserById(String userId) {
		Optional<User> byId = repository.findById(userId);
		User user = null;
		if(byId.isPresent()) {
			user = byId.get();
		}
		return user;
	}

	@Override
	public boolean deleteById(String userId) {
		repository.deleteById(userId);
		return true;
	}

	@Override
	public boolean updateUserById(User user) {
		repository.save(user);
		return true;
	}
}
