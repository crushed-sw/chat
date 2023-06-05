package com.chat.entity.replay;

import com.chat.entity.Chitchat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回聊天记录的JSON实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayChat {
	List<Chitchat> chatRecords;
}
