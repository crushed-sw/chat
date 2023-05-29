package com.chat.mapper.impl;

import com.chat.entity.Chitchat;
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
		MongoCollection<Document> users = MongoUtil.getDatabase("group");
		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("crew", userId));
		users.updateOne(new BasicDBObject("_id", groupId), userObject);
	}

	@Override
	public void deleteCrew(String groupId, String userId) {
		MongoCollection<Document> users = MongoUtil.getDatabase("group");
		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$pull", new BasicDBObject("crew", userId));
		users.updateOne(new BasicDBObject("_id", groupId), userObject);
	}

	@Override
	public void appendRecord(String groupId, String userId, String message) {
		MongoCollection<Document> users = MongoUtil.getDatabase("group");

		Chitchat chat = new Chitchat(new Date(System.currentTimeMillis()), userId, message);
		BasicDBObject obj = new BasicDBObject();
		obj.put("date", chat.getDate());
		obj.put("from", chat.getFrom());
		obj.put("chat", chat.getChat());

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("record", obj));
		users.updateOne(new BasicDBObject("_id", groupId), userObject);
	}

	@Override
	public void updateName(String groupId, String newName) {
		MongoCollection<Document> users = MongoUtil.getDatabase("group");

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$set", new BasicDBObject("name", newName));
		users.updateOne(new BasicDBObject("_id", groupId), userObject);
	}
}
