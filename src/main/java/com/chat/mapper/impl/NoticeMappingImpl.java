package com.chat.mapper.impl;

import com.chat.entity.Notice;
import com.chat.entity.NoticeMessage;
import com.chat.mapper.NoticeMapping;
import com.chat.mapper.NoticeRepository;
import com.chat.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * notice数据库crud
 */
@Repository
public class NoticeMappingImpl implements NoticeMapping {
	@Autowired
	NoticeRepository noticeRepository;

	/**
	 * 添加通知消息
	 * @param userId 用户ID
	 * @param message 添加的通知内容
	 */
	@Override
	public void addMessage(String userId, NoticeMessage message) {
		MongoCollection<Document> notice = MongoUtil.getDatabase("notice");

		BasicDBObject messageObject = new BasicDBObject();
		messageObject.put("status", message.getStatus());
		messageObject.put("fromId", message.getFromId());
		messageObject.put("avatar", message.getAvatar());
		messageObject.put("name", message.getName());
		messageObject.put("message", message.getMessage());
		messageObject.put("groupName", message.getGroupName());
		messageObject.put("groupId", message.getGroupId());

		BasicDBObject userObject = new BasicDBObject();
		userObject.append("$push", new BasicDBObject("notice", messageObject));
		notice.updateOne(new BasicDBObject("_id", userId), userObject);
	}

	/**
	 * 更改通知消息
	 * @param userId 用户ID
	 * @param fromId 操作的消息所指对象
	 * @param status 需要更改的状态
	 * @param newStatus 需要更改为的状态
	 */
	@Override
	public void updateMessage(String userId, String fromId, Integer status, Integer newStatus) {
		Optional<Notice> byId = noticeRepository.findById(userId);
		Notice notice = null;
		if(byId.isPresent()) {
			notice = byId.get();
		}
		assert(notice != null);
		for (NoticeMessage noticeMessage : notice.getNotice()) {
			if(noticeMessage.getFromId().equals(fromId.trim()) && noticeMessage.getStatus().equals(status)) {
				noticeMessage.setStatus(newStatus);
			}
		}
		noticeRepository.save(notice);
	}
}
