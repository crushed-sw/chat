package com.chat.entity.replay;

import com.chat.entity.Chitchat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayChat {
	List<Chitchat> chatRecords;
}
