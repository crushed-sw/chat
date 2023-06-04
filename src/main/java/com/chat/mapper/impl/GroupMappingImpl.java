package com.chat.mapper.impl;

import com.alibaba.fastjson.JSON;
import com.chat.entity.Chitchat;
import com.chat.entity.FriendChatRecord;
import com.chat.entity.GroupChatRecord;
import com.chat.entity.User;
import com.chat.mapper.GroupMapping;
import com.chat.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
		obj.put("userId", user.getUserId());
		obj.put("chat", message);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("record", obj));
		group.updateOne(new BasicDBObject("_id", groupId), userObject);
	}

	@Override
	public GroupChatRecord getRecord(String groupId, int start, int number) {
		MongoCollection<Document> friend = MongoUtil.getDatabase("group");

		BasicDBObject query = new BasicDBObject();
		query.put("_id", groupId);

		BasicDBObject projection = new BasicDBObject();
		projection.put("record", new BasicDBObject("$slice", new int[]{start, number}));

		MongoCursor<Document> iterator = friend.find(query).projection(projection).iterator();

		GroupChatRecord groupChatRecord = null;
		if(iterator.hasNext()) {
			Document next = iterator.next();
			next.append("groupId", next.get("_id"));

			groupChatRecord = JSON.parseObject(next.toJson(), GroupChatRecord.class);
		}
		return groupChatRecord;
	}

	@Override
	public int getSizeOfRecord(String groupId) {
		MongoCollection<Document> friend = MongoUtil.getDatabase("friend");

		BasicDBObject query = new BasicDBObject();
		query.put("_id", groupId);

		BasicDBObject projection = new BasicDBObject();
		projection.put("recordSize", new BasicDBObject("$size", "$record"));

		MongoCursor<Document> iterator = friend.find(query).projection(projection).iterator();
		int result = 0;
		if(iterator.hasNext()) {
			result = iterator.next().getInteger("recordSize");
		}

		return result;
	}
}