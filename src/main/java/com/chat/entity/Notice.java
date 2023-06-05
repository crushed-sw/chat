package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 通知实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notice")
public class Notice {
	@Id
	private String userId;
	List<NoticeMessage> notice;
}
