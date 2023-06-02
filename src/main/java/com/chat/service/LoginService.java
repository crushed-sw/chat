package com.chat.service;

import com.chat.entity.replay.ReplayLoginMessage;

public interface LoginService {
	ReplayLoginMessage getReplayLoginMessage(String userId, String password);
}
