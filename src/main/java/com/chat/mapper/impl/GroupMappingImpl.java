package com.chat.mapper.impl;

import com.chat.entity.Chitchat;
import com.chat.entity.User;
import com.chat.mapper.GroupMapping;
import com.chat.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class GroupMappingImpl implements GroupMapping {
	@Override
	public void appendCrew(String groupId, String userId) {
		MongoCollection<Document> group = MongoUtil.getDatabase("group");
		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("crew", userId));
		group.updateOne(new BasicDBObject("_id", groupId), userObject);
	}

	@Override
	public void deleteCrew(String groupId, String userId) {
		MongoCollection<Document> group = MongoUtil.getDatabase("group");
		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$pull", new BasicDBObject("crew", userId));
		group.updateOne(new BasicDBObject("_id", groupId), userObject);
	}

	@Override
	public void appendRecord(String groupId, User user, String message, String date) {
		MongoCollection<Document> group = MongoUtil.getDatabase("group");

		BasicDBObject obj = new BasicDBObject();
		obj.put("date", date);
		obj.put("avatar", user.getAvatar());
		obj.put("name", user.getUserName());
		obj.put("from", user.getUserId());
		obj.put("chat", message);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("record", obj));
		group.updateOne(new BasicDBObject("_id", groupId), userObject);
	}

	@Override
	public void updateName(String groupId, String newName) {
		MongoCollection<Document> group = MongoUtil.getDatabase("group");

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$set", new BasicDBObject("name", newName));
		group.updateOne(new BasicDBObject("_id", groupId), userObject);
	}
}
