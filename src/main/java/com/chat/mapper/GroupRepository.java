package com.chat.mapper;

import com.chat.entity.GroupChatRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<GroupChatRecord, String> {
}
