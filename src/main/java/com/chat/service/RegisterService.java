package com.chat.service;

import com.chat.entity.replay.ReplayRegisterMessage;

public interface RegisterService {
	ReplayRegisterMessage getRegisterMessage(String userId, String password, String userName);
}
