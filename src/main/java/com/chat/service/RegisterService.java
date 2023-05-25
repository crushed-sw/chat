package com.chat.service;

import com.chat.entity.ReplayRegisterMessage;

public interface RegisterService {
	ReplayRegisterMessage getREgisterMessage(String userId, String password, String userName);
}
