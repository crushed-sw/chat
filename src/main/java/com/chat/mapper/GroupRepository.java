package com.chat.mapper;

import com.chat.entity.GroupChatRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<GroupChatRecord, String> {
}
