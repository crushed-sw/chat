package com.chat.mapper.impl;

import com.chat.mapper.UserMapping;
import com.chat.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class UserMappingImpl implements UserMapping {
	@Override
	public void appendGroup(String userId, String groupId) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("groups", groupId));
		users.updateOne(new BasicDBObject("_id", userId), userObject);
	}

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

	@Override
	public void deleteFriendGroup(String userId, String groupName) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("groupName", groupName);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$pull", new BasicDBObject("friendGroups", friendGroupObject));

		users.updateOne(new BasicDBObject("_id", userId), userObject);
	}

	@Override
	public void deleteGroup(String userId, String groupId) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("groups", groupId);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$pull",friendGroupObject);

		users.updateOne(new BasicDBObject("_id", userId), userObject);
	}

	@Override
	public void updateFriendGroup(String userId, String groupName, String newGroupName) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject queryObject = new BasicDBObject();
		queryObject.put("_id", userId);
		queryObject.put("friendGroups.groupName", groupName);

		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("friendGroups.$.groupName", newGroupName);

		BasicDBObject userObject = new BasicDBObject();
		userObject.put("$set", friendGroupObject);

		users.updateOne(queryObject, userObject);
	}

}
