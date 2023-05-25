package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
public class ParticipantRepository {
	private Map<String, User> activeSession = new ConcurrentHashMap<>();

	public void add(String userId, User user) {
		activeSession.put(userId, user);
	}

	public User remove(String userId) {
		return activeSession.remove(userId);
	}

	public boolean containUserId(String userId) {
		return activeSession.containsKey(userId);
	}
}
