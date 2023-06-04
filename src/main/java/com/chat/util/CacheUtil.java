package com.chat.util;

import com.chat.entity.User;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class CacheUtil {
	private static final Map<String, User> userCache = new ConcurrentHashMap<>();

	public static User getUserInCache(String userId) {
		return userCache.get(userId);
	}

	public static void setUserInCache(User user) {
		deleteCache(userCache, 1000);
		userCache.put(user.getUserId(), user);
	}

	private static void deleteCache(Map<String, ?> map, int maxsize) {
		if(map.size() >= maxsize) {
			Random random = new Random();

			int randomIndex = random.nextInt(map.size());
			map.entrySet().stream()
					.skip(randomIndex)
					.findFirst()
					.ifPresent(entry -> map.remove(entry.getKey()));
		}
	}
}
