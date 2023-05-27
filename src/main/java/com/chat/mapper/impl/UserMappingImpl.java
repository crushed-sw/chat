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
	public void appendGroup(String userId, String groupId) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("groups", groupId));
		users.updateOne(new BasicDBObject("_id", userId), userObject);
	}

	public void appendFriendGroup(String userId, String groupName) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");
		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("groupName", groupName);
		friendGroupObject.put("friends", new ArrayList<>());

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("friendGroups", friendGroupObject));
		users.updateOne(new BasicDBObject("_id", userId), userObject);
	}

	public void appendFriend(String userId, String groupName, String friendId, String remarks, String friendEachId) {
		MongoCollection<Document> users = MongoUtil.getDatabase("user");

		BasicDBObject friendGroupObject = new BasicDBObject();
		friendGroupObject.put("remarks", remarks);
		friendGroupObject.put("friendId", friendId);
		friendGroupObject.put("friendEachId", friendEachId);

		BasicDBObject queryObject = new BasicDBObject();
		queryObject.put("_id", userId);
		queryObject.put("friendGroups.groupName", groupName);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("friendGroups.$.friends", friendGroupObject));

		users.updateOne(queryObject, userObject);
	}
}
