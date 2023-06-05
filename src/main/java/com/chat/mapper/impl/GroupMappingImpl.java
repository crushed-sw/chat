package com.chat.mapper.impl;

import com.alibaba.fastjson.JSON;
import com.chat.entity.GroupChatRecord;
import com.chat.entity.User;
import com.chat.mapper.GroupMapping;
import com.chat.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Repository;

/**
 * group数据库的crud
 */
@Repository
public class GroupMappingImpl implements GroupMapping {
	/**
	 * 添加群聊成员
	 * @param groupId 群聊ID
	 * @param userId 添加的成员ID
	 */
	@Override
	public void appendCrew(String groupId, String userId) {
		MongoCollection<Document> group = MongoUtil.getDatabase("group");
		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("crew", userId));
		group.updateOne(new BasicDBObject("_id", groupId), userObject);
	}

	/**
	 * 删除群聊成员
	 * @param groupId 群聊ID
	 * @param userId 删除的成员ID
	 */
	@Override
	public void deleteCrew(String groupId, String userId) {
		MongoCollection<Document> group = MongoUtil.getDatabase("group");
		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$pull", new BasicDBObject("crew", userId));
		group.updateOne(new BasicDBObject("_id", groupId), userObject);
	}

	/**
	 * 添加群聊聊天记录
	 * @param groupId 群聊ID
	 * @param user 添加该聊天记录成员
	 * @param message 聊天内容
	 * @param date 发送消息时间
	 */
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

	/**
	 * 获取群聊聊天内容
	 * @param groupId 群聊ID
	 * @param start 第几条开始请求
	 * @param number 总共请求number条
	 * @return 该群聊的第start到start+number条消息
	 */
	@Override
	public GroupChatRecord getRecord(String groupId, int start, int number) {
		MongoCollection<Document> group = MongoUtil.getDatabase("group");

		BasicDBObject query = new BasicDBObject();
		query.put("_id", groupId);

		BasicDBObject projection = new BasicDBObject();
		projection.put("record", new BasicDBObject("$slice", new int[]{start, number}));

		MongoCursor<Document> iterator = group.find(query).projection(projection).iterator();

		GroupChatRecord groupChatRecord = null;
		if(iterator.hasNext()) {
			Document next = iterator.next();
			next.append("groupId", next.get("_id"));

			groupChatRecord = JSON.parseObject(next.toJson(), GroupChatRecord.class);
		}
		return groupChatRecord;
	}

	/**
	 * 群聊聊天记录的数量
	 * @param groupId 群聊ID
	 * @return 群聊聊天记录的数量
	 */
	@Override
	public int getSizeOfRecord(String groupId) {
		MongoCollection<Document> group = MongoUtil.getDatabase("group");

		BasicDBObject query = new BasicDBObject();
		query.put("_id", groupId);

		BasicDBObject projection = new BasicDBObject();
		projection.put("recordSize", new BasicDBObject("$size", "$record"));

		MongoCursor<Document> iterator = group.find(query).projection(projection).iterator();
		int result = 0;
		if(iterator.hasNext()) {
			result = iterator.next().getInteger("recordSize");
		}

		return result;
	}
}