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
	public static List<String> userPicture;
	public static List<String> groupPicture;

	static {
		userPicture = new ArrayList<>();
		groupPicture = new ArrayList<>();

		userPicture.add("https://pic.imgdb.cn/item/647dfe3f1ddac507cc775e0f.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfe3f1ddac507cc775db8.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfe3f1ddac507cc775d54.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfe3f1ddac507cc775d35.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfe3f1ddac507cc775d09.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfd7a1ddac507cc75a341.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfd7a1ddac507cc75a29a.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfd7a1ddac507cc75a271.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfd7a1ddac507cc75a23f.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfd7a1ddac507cc75a1cf.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfccb1ddac507cc74211d.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfcc91ddac507cc741d75.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfcc81ddac507cc741b12.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfcc81ddac507cc7419ad.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfcc61ddac507cc741626.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfbcf1ddac507cc71e11a.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfbcf1ddac507cc71e0dc.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfbce1ddac507cc71e0a0.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfbce1ddac507cc71e055.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfbce1ddac507cc71e040.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfb1b1ddac507cc704fa8.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfb1b1ddac507cc704e32.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfb1b1ddac507cc704ddc.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfb161ddac507cc7043cf.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfb161ddac507cc70431e.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfa6b1ddac507cc6eb4fc.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfa6b1ddac507cc6eb4d3.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfa6b1ddac507cc6eb455.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfa6b1ddac507cc6eb420.png");
		userPicture.add("https://pic.imgdb.cn/item/647dfa6b1ddac507cc6eb3f1.png");
		userPicture.add("https://pic.imgdb.cn/item/647df8a31ddac507cc6a7fbc.png");
		userPicture.add("https://pic.imgdb.cn/item/647df8a31ddac507cc6a7eca.png");
		userPicture.add("https://pic.imgdb.cn/item/647df8a31ddac507cc6a7e4c.png");
		userPicture.add("https://pic.imgdb.cn/item/647df8a31ddac507cc6a7dd3.png");
		userPicture.add("https://pic.imgdb.cn/item/647df8a31ddac507cc6a7d56.png");
		userPicture.add("https://pic.imgdb.cn/item/647df82a1ddac507cc692b46.png");
		userPicture.add("https://pic.imgdb.cn/item/647df8271ddac507cc6925b0.png");
		userPicture.add("https://pic.imgdb.cn/item/647df8261ddac507cc69227d.png");
		userPicture.add("https://pic.imgdb.cn/item/647df8261ddac507cc692220.png");
		userPicture.add("https://pic.imgdb.cn/item/647df8251ddac507cc692037.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6bf1ddac507cc65efd1.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6bb1ddac507cc65e57f.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6bb1ddac507cc65e457.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6bb1ddac507cc65e3fb.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6b51ddac507cc65d8f8.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6071ddac507cc64418e.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6071ddac507cc64414f.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6071ddac507cc644110.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6071ddac507cc6440ee.png");
		userPicture.add("https://pic.imgdb.cn/item/647df6071ddac507cc6440c3.png");

		groupPicture.add("https://pic.imgdb.cn/item/647e055a1ddac507cc86ee19.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e053a1ddac507cc86adc0.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e053a1ddac507cc86ad7c.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e053a1ddac507cc86ad55.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e053a1ddac507cc86ad1c.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e053a1ddac507cc86ad0b.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e052a1ddac507cc868c76.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e052a1ddac507cc868c35.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e052a1ddac507cc868c0d.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e052a1ddac507cc868c03.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e052a1ddac507cc868baf.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e05201ddac507cc8678bb.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e05201ddac507cc867807.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e05201ddac507cc8676af.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e051e1ddac507cc86737a.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e051e1ddac507cc86720d.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e050f1ddac507cc865543.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e050f1ddac507cc8654f9.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e050f1ddac507cc8654b5.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e050f1ddac507cc865490.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e050f1ddac507cc865453.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04fe1ddac507cc8634a2.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04fe1ddac507cc863473.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04fe1ddac507cc86339e.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04fe1ddac507cc863341.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04fe1ddac507cc8632e3.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04eb1ddac507cc860dd5.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04eb1ddac507cc860da7.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04ea1ddac507cc860cb2.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04ea1ddac507cc860c1c.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04ea1ddac507cc860bc8.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04d81ddac507cc85ea2f.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04d81ddac507cc85ea03.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04d81ddac507cc85e9ca.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04d81ddac507cc85e9a3.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e04d71ddac507cc85e7c1.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e03591ddac507cc82e088.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e03581ddac507cc82ddcb.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e03581ddac507cc82dc9f.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e03571ddac507cc82da1a.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e03551ddac507cc82d61f.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e033f1ddac507cc82a3b4.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e033f1ddac507cc82a34f.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e033f1ddac507cc82a318.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e033f1ddac507cc82a2fc.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e033f1ddac507cc82a2d4.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e03011ddac507cc821f20.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e02fc1ddac507cc82152f.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e02fb1ddac507cc821372.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e02f11ddac507cc81fe95.png");
		groupPicture.add("https://pic.imgdb.cn/item/647e02f01ddac507cc81fced.png");
	}

	/**
	 * 随机获取用户头像
	 * @return
	 */
	public static String getUserAvatar() {
		Random random = new Random();
		return userPicture.get(random.nextInt(49));
	}

	/**
	 * 随机获取群聊头像
	 * @return
	 */
	public static String getGroupAvatar() {
		Random random = new Random();
		return groupPicture.get(random.nextInt(50));
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
		String friendEachId = "1000001";
		try {
			friendEachId = (String) redisUtil.get("friendEachId");
		} catch (Exception e) {
			redisUtil.set("friendEachId", "1000001");
		}
		return friendEachId;
	}

	/**
	 * 获取最小可用groupId
	 * @return
	 */
	public String getGroupId() {
		RedisUtil redisUtil = new RedisUtil();
		String groupId = "100001";
		try {
			groupId = (String) redisUtil.get("groupId");
		} catch (Exception e) {
			redisUtil.set("groupId", "100001");
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

	/**
	 * 将map转为适当的JSON
	 * @param map
	 * @return
	 */
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
