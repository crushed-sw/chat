package com.chat.mapper.impl;

import com.chat.mapper.UserMapping;
import com.chat.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * user数据库crud
 */
@Repository
public class UserMappingImpl implements UserMapping {
	/**
	 * 添加群聊ID
	 * @param userId 用户ID
	 * @param groupId 群聊ID
	 */
	@Override
	public void appendGroup(String userId, String groupId) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("groups", groupId));
		users.updateOne(new BasicDBObject("_id", userId), userObject);
	}

	/**
	 * 添加好友分组
	 * @param userId 用户ID
	 * @param groupName 新好友分组名字
	 */
	@Override
	public void appendFriendGroup(String userId, String groupName) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("groupName", groupName);
		friendGroupObject.put("friends", new ArrayList<>());

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("friendGroups", friendGroupObject));
		users.updateOne(new BasicDBObject("_id", userId), userObject);
	}

	/**
	 * 添加好友
	 * @param userId 用户ID
	 * @param groupName 好友分组
	 * @param friendId 好友ID
	 * @param friendEachId 用户与好友之间的ID
	 * @param friendName 好友分组
	 * @param avatar 好友头像
	 */
	@Override
	public void appendFriend(String userId, String groupName, String friendId, String friendEachId, String friendName, String avatar) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");

		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("avatar", avatar);
		friendGroupObject.put("friendId", friendId);
		friendGroupObject.put("friendName", friendName);
		friendGroupObject.put("friendEachId", friendEachId);

		BasicDBObject queryObject = new BasicDBObject();
		queryObject.put("_id", userId);
		queryObject.put("friendGroups.groupName", groupName);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("friendGroups.$.friends", friendGroupObject));

		users.updateOne(queryObject, userObject);
	}

	/**
	 * 删除好友
	 * @param userId 用户ID
	 * @param groupName 好友分组
	 * @param friendId 好友ID
	 */
	@Override
	public void deleteFriend(String userId, String groupName, String friendId) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");

		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("friendId", friendId);

		BasicDBObject queryObject = new BasicDBObject();
		queryObject.put("_id", userId);
		queryObject.put("friendGroups.groupName", groupName);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$pull", new BasicDBObject("friendGroups.$.friends", friendGroupObject));

		users.updateOne(queryObject, userObject);
	}

	/**
	 * 删除好友分组
	 * @param userId 用户ID
	 * @param groupName 好友分组
	 */
	@Override
	public void deleteFriendGroup(String userId, String groupName) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("groupName", groupName);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$pull", new BasicDBObject("friendGroups", friendGroupObject));

		users.updateOne(new BasicDBObject("_id", userId), userObject);
	}

	/**
	 * 删除群聊
	 * @param userId 用户ID
	 * @param groupId 群聊ID
	 */
	@Override
	public void deleteGroup(String userId, String groupId) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("groups", groupId);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$pull",friendGroupObject);

		users.updateOne(new BasicDBObject("_id", userId), userObject);
	}
}
