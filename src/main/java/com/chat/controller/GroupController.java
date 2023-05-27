package com.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.chat.anntation.UserLoginToken;
import com.chat.entity.ReplayGroup;
import com.chat.entity.UserInFriend;
import com.chat.mapper.GroupRepository;
import com.chat.service.FriendService;
import com.chat.service.GroupService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/group")
public class GroupController {
	@Autowired
	GroupService groupService;

	@UserLoginToken
	@GetMapping
	public ReplayGroup getGroups(HttpServletRequest req) {
		JSONObject json = (JSONObject) req.getAttribute("requestJson");
		ReplayGroup replay = new ReplayGroup();
		replay.setGroups(groupService.getgroupsById(json.getString("userId")));
		return replay;
	}

	@PostMapping
	public ReplayGroup addGroup() {
		return null;
	}

	@DeleteMapping
	public ReplayGroup deleteGroup() {
		return null;
	}

	@PutMapping
	public ReplayGroup updateGroup() {
		return null;
	}
}
