package com.chat.service.impl;

import com.chat.entity.Friend;
import com.chat.entity.User;
import com.chat.entity.UserInFriend;
import com.chat.mapper.UserMapping;
import com.chat.mapper.UserRepository;
import com.chat.service.UserService;
import com.chat.util.CacheUtil;
import com.chat.util.RedisUtil;
import net.sf.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repository;

	@Override
	public void insertUser(String userId, String userName, String password) {
		repository.save(new User(userId, userName, password));
	}

	@Override
	public User getUserById(String userId) {
		User user = CacheUtil.getUserInCache(userId);
		return user == null ? getUserByIdInDatabase(userId) : user;
	}

	private User getUserByIdInDatabase(String userId) {
		Optional<User> byId = repository.findById(userId);
		User user = null;
		if(byId.isPresent()) {
			user = byId.get();
		}
		return user;
	}
}
