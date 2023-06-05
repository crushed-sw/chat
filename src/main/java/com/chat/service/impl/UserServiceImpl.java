package com.chat.service.impl;

import com.chat.entity.User;
import com.chat.mapper.UserRepository;
import com.chat.service.UserService;
import com.chat.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户相关逻辑类
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repository;

	/**
	 * 给数据库添加用户信息
	 * @param userId 用户ID
	 * @param userName 用户名称
	 * @param password 密码
	 */
	@Override
	public void insertUser(String userId, String userName, String password) {
		repository.save(new User(userId, userName, password));
	}

	/**
	 * 获取用户
	 * @param userId 用户ID
	 * @return 用户实体类
	 */
	@Override
	public User getUserById(String userId) {
		User user = CacheUtil.getUserInCache(userId);
		return user == null ? getUserByIdInDatabase(userId) : user;
	}

	/**
	 * 从数据库获取用户
	 * @param userId 用户ID
	 * @return 用户实体类
	 */
	private User getUserByIdInDatabase(String userId) {
		Optional<User> byId = repository.findById(userId);
		User user = null;
		if(byId.isPresent()) {
			user = byId.get();
		}
		return user;
	}
}
