package com.chat.mapper.impl;

import com.chat.entity.Chitchat;
import com.chat.mapper.FriendMapping;
import com.chat.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class FriendMappingImpl implements FriendMapping {

	@Override
	public void appendRecord(String eachId, String userId, String message) {
		MongoCollection<Document> users = MongoUtil.getDatabase("friend");

		Chitchat chat = new Chitchat(new Date(System.currentTimeMillis()), userId, message);
		BasicDBObject obj = new BasicDBObject();
		obj.put("date", chat.getDate());
		obj.put("from", chat.getFrom());
		obj.put("chat", chat.getChat());

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("record", obj));
		users.updateOne(new BasicDBObject("_id", eachId), userObject);
	}
}
