package com.chat.mapper.impl;

import com.alibaba.fastjson.JSON;
import com.chat.entity.FriendChatRecord;
import com.chat.entity.User;
import com.chat.mapper.FriendMapping;
import com.chat.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Repository;

/**
 * friend数据库的crud
 */
@Repository
public class FriendMappingImpl implements FriendMapping {

	/**
	 * 添加一条好友聊天记录
	 * @param eachId 好友之间的id
	 * @param user 请求添加聊天记录的用户
	 * @param message 添加的聊天记录内容
	 * @param date 添加聊天记录的时间
	 */
	@Override
	public void appendRecord(String eachId, User user, String message, String date) {
		MongoCollection<Document> friend = MongoUtil.getDatabase("friend");

		BasicDBObject obj = new BasicDBObject();
		obj.put("date", date);
		obj.put("avatar", user.getAvatar());
		obj.put("name", user.getUserName());
		obj.put("userId", user.getUserId());
		obj.put("chat", message);

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("record", obj));
		friend.updateOne(new BasicDBObject("_id", eachId), userObject);
	}

	/**
	 * 获取好友之间的聊天记录
	 * @param eachId 好友之间的id
	 * @param start 第几条开始请求
	 * @param number 总共请求number条
	 * @return 该好友之间的第start到start+number条消息
	 */
	@Override
	public FriendChatRecord getRecord(String eachId, int start, int number) {
		MongoCollection<Document> friend = MongoUtil.getDatabase("friend");

		BasicDBObject query = new BasicDBObject();
		query.put("_id", eachId);

		BasicDBObject projection = new BasicDBObject();
		projection.put("record", new BasicDBObject("$slice", new int[]{start, number}));

		MongoCursor<Document> iterator = friend.find(query).projection(projection).iterator();

		FriendChatRecord friendChatRecord = null;
		if(iterator.hasNext()) {
			Document next = iterator.next();
			next.append("friendId", next.get("_id"));

			friendChatRecord = JSON.parseObject(next.toJson(), FriendChatRecord.class);
		}
		return friendChatRecord;
	}

	/**
	 * 好友之间聊天记录的数量
	 * @param eachId 好友之间的id
	 * @return 好友之间聊天记录的数量
	 */
	@Override
	public int getSizeOfRecord(String eachId) {
		MongoCollection<Document> friend = MongoUtil.getDatabase("friend");

		BasicDBObject query = new BasicDBObject();
		query.put("_id", eachId);

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
