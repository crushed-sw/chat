package com.chat.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 通用工具类
 */
@Component
public class CommondUtil {
	@Autowired
	RedisUtil redisUtil;
	public static List<String> picture;

	static {
		picture = new ArrayList<>();
		picture.add("https://pic1.imgdb.cn/item/647217bff024cca17377d8bb.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217bef024cca17377d8a6.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217bef024cca17377d894.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217bef024cca17377d8ac.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217bef024cca17377d8a3.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b8f024cca17377d227.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b8f024cca17377d1ed.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b8f024cca17377d1e3.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b8f024cca17377d1d9.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b8f024cca17377d1d5.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b1f024cca17377ca57.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b1f024cca17377ca38.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b1f024cca17377ca0c.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b1f024cca17377c9e0.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217b1f024cca17377c9dd.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217aaf024cca17377c31a.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217aaf024cca17377c316.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217aaf024cca17377c310.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217aaf024cca17377c2e5.jpg");
		picture.add("https://pic1.imgdb.cn/item/647217aaf024cca17377c2c0.jpg");
		picture.add("https://pic1.imgdb.cn/item/6472179ff024cca17377b6e9.jpg");
		picture.add("https://pic1.imgdb.cn/item/6472179ff024cca17377b6c8.jpg");
		picture.add("https://pic1.imgdb.cn/item/6472179ff024cca17377b6c5.jpg");
		picture.add("https://pic1.imgdb.cn/item/6472179ff024cca17377b6ba.jpg");
		picture.add("https://pic1.imgdb.cn/item/6472179ff024cca17377b6b6.jpg");
		picture.add("https://pic1.imgdb.cn/item/64721789f024cca173779f19.jpg");
		picture.add("https://pic1.imgdb.cn/item/64721789f024cca173779f12.jpg");
		picture.add("https://pic1.imgdb.cn/item/64721789f024cca173779f10.jpg");
		picture.add("https://pic1.imgdb.cn/item/64721789f024cca173779f02.jpg");
		picture.add("https://pic1.imgdb.cn/item/64721789f024cca173779eff.jpg");
	}

	public static String getAvatar() {
		Random random = new Random();
		return picture.get(random.nextInt(30));
	}

	/**
	 * 判断字符串是否为空
	 * @param string
	 * @return
	 */
	public static boolean judgeStringEmpty(String string) {
		return string == null || string.trim().equals("");
	}

	/**
	 * 获取最小可用friendEachId
	 * @return
	 */
	public String getFriendEachId() {
		RedisUtil redisUtil = new RedisUtil();
		String friendEachId = "1";
		try {
			friendEachId = (String) redisUtil.get("friendEachId");
		} catch (Exception e) {
			redisUtil.set("friendEachId", "1");
		}
		return friendEachId;
	}

	/**
	 * 更新最小可用friendEachId
	 */
	public void incFriendEachId() {
		RedisUtil redisUtil = new RedisUtil();
		int id = Integer.parseInt(getFriendEachId());
		id++;
		redisUtil.set("friendEachId", Integer.toString(id));
	}

	/**
	 * 获取最小可用groupId
	 * @return
	 */
	public String getGroupId() {
		RedisUtil redisUtil = new RedisUtil();
		String groupId = "1";
		try {
			groupId = (String) redisUtil.get("groupId");
		} catch (Exception e) {
			redisUtil.set("groupId", "1");
		}
		return groupId;
	}

	/**
	 * 更新最小可用groupId
	 */
	public void incGroupId() {
		RedisUtil redisUtil = new RedisUtil();
		int id = Integer.parseInt(getGroupId());
		id++;
		redisUtil.set("groupId", Integer.toString(id));
	}

	public static String mapToJson(Map<Object, Object> map) {
		StringBuilder sb = new StringBuilder("{");
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			sb.append("\"").append(entry.getKey()).append("\":");
			if(entry.getKey().equals("notice")) {
				sb.append("\"").append(entry.getValue()).append("\",");
			} else {
				sb.append(entry.getValue()).append(",");
			}
		}
		if(sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}
}
