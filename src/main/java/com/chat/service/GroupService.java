package com.chat.service;

import com.chat.entity.User;

import java.util.List;

public interface GroupService {
	List<String> getgroupsById(String userId);
}
