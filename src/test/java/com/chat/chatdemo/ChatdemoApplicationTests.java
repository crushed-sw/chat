package com.chat.chatdemo;

import com.chat.entity.GroupChatRecord;
import com.chat.entity.Notice;
import com.chat.entity.NoticeMessage;
import com.chat.entity.User;
import com.chat.mapper.*;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import com.chat.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatdemoApplicationTests {

}
