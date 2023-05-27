package com.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.chat.anntation.UserLoginToken;
import com.chat.entity.ReplayFriend;
import com.chat.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat/friend")
public class FriendController {
	@Autowired
	FriendService friendService;

	@UserLoginToken
	@GetMapping
	public ReplayFriend getFriends(HttpServletRequest req) {
		JSONObject json = (JSONObject) req.getAttribute("requestJson");
		ReplayFriend replay = new ReplayFriend();
		replay.setFriends(friendService.getFriendsById(json.getString("userId")));
		return replay;
	}

	@PostMapping
	public ReplayFriend addFriend() {
		return null;
	}

	@DeleteMapping
	public ReplayFriend deleteFriend() {
		return null;
	}

	@PutMapping
	public ReplayFriend updateFriend() {
		return null;
	}
}
