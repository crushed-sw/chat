package com.chat.service;

import com.chat.entity.ReplayLoginMessage;

public interface LoginService {
	ReplayLoginMessage getReplayLoginMessage(String userId, String password);
}
