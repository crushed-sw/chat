package com.chat.service.impl;

import com.chat.entity.FriendChatRecord;
import com.chat.entity.User;
import com.chat.entity.UserInFriend;
import com.chat.mapper.FriendRepository;
import com.chat.mapper.UserRepository;
import com.chat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {
	@Autowired
	FriendRepository friendRepository;
	@Autowired
	UserRepository userRepository;

	@Override
	public void insertFriend(String userId, String friendId, String groupName) {
		friendRepository.save(new FriendChatRecord(1L, userId, friendId));

	}

	@Override
	public List<UserInFriend> getFriendsById(String userId) {
		Optional<User> opt = userRepository.findById(userId);
		User user = null;
		if(opt.isPresent()) {
			user = opt.get();
		}

		assert user != null;
		return user.getFriendGroups();
	}

	@Override
	public void deleteById(String userId) {

	}

	@Override
	public void updateUserById(User user) {

	}
}
