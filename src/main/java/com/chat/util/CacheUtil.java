package com.chat.util;

import com.chat.entity.User;
import com.chat.mapper.UserMapping;
import com.chat.mapper.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存工具类
 */
@Component
public class CacheUtil {
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserMapping userMapping;

	private static final Map<String, User> userCache = new ConcurrentHashMap<>();

	/**
	 * 查看缓存并从缓存获取用户类
	 * @param userId 用户ID
	 * @return 用户类
	 */
	public User getUserInCache(String userId) {
		return userCache.get(userId);
	}

	/**
	 * 将用户添加到缓存
	 * @param user 用户类
	 */
	public void setUserInCache(String userId, User user) {
		userCache.put(userId, user);

		if(userCache.size() >= 1000) {
			Random random = new Random();

			int randomIndex = random.nextInt(userCache.size());
			userCache.entrySet().stream()
					.skip(randomIndex)
					.findFirst()
					.ifPresent(entry -> {
						userRepository.save(entry.getValue());
						userCache.remove(entry.getKey());
					});
		}
	}

	public void deleteUserInCache(String userId) {
		userCache.remove(userId);
	}

	public boolean isInCache(String userId) {
		User user = getUserInCache(userId);
		return user != null;
	}
}
