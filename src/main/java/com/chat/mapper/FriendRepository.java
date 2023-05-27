package com.chat.mapper;

import com.chat.entity.FriendChatRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FriendRepository extends MongoRepository<FriendChatRecord, String> {
}
