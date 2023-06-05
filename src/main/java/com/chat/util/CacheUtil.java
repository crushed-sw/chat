package com.chat.util;

import com.chat.entity.User;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存工具类
 */
public class CacheUtil {
	private static final Map<String, User> userCache = new ConcurrentHashMap<>();

	/**
	 * 查看缓存并从缓存获取用户类
	 * @param userId 用户ID
	 * @return 用户类
	 */
	public static User getUserInCache(String userId) {
		return userCache.get(userId);
	}

	/**
	 * 将用户添加到缓存
	 * @param user 用户类
	 */
	public static void setUserInCache(User user) {
		deleteCache(userCache, 1000);
		userCache.put(user.getUserId(), user);
	}

	/**
	 * 如果缓存已满，随机删除一个缓存对象
	 * @param map 缓存
	 * @param maxsize 最大缓存数量
	 */
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
