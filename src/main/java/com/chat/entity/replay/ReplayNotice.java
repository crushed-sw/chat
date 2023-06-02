package com.chat.entity.replay;

import com.chat.entity.NoticeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayNotice {
	List<NoticeMessage> notice;
}
