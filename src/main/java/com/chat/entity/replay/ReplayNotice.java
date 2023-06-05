package com.chat.entity.replay;

import com.chat.entity.NoticeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回通知的JSON实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayNotice {
	List<NoticeMessage> notice;
}
