package com.chat.controller;

import com.chat.anntation.UserLoginToken;
import com.chat.entity.GroupMessageJson;
import com.chat.entity.ReplayGroup;
import com.chat.service.GroupService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat/group")
public class GroupController {
	@Autowired
	GroupService groupService;

	@UserLoginToken
	@PostMapping
	public ReplayGroup getGroups(HttpServletRequest req) {
		return groupService.getReplayGroup((String) req.getAttribute("userId"));
	}

	@UserLoginToken
	@DeleteMapping
	public ReplayGroup deleteGroup(@RequestBody GroupMessageJson groupMessageJson) {
		return groupService.getReplayGroup(groupMessageJson.getUserId());
	}
}
