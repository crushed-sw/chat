package com.chat.service.impl;

import com.chat.entity.User;
import com.chat.mapper.UserRepository;
import com.chat.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
	@Autowired
	UserRepository userRepository;

	@Override
	public List<String> getgroupsById(String userId) {
		Optional<User> opt = userRepository.findById(userId);
		User user = null;
		if(opt.isPresent()) {
			user = opt.get();
		}

		assert user != null;
		return user.getGroups();
	}
}
