package com.chat.mapper.impl;

import com.chat.entity.Chitchat;
import com.chat.entity.User;
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
	public void appendRecord(String eachId, User user, String message, String date) {
		MongoCollection<Document> friend = MongoUtil.getDatabase("friend");

		BasicDBObject obj = new BasicDBObject();
		obj.put("date", date);
		obj.put("avatar", user.getAvatar());
		obj.put("name", user.getUserName());
		obj.put("id", user.getUserId());
		obj.put("chat", message);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("record", obj));
		friend.updateOne(new BasicDBObject("_id", eachId), userObject);
	}
}
