package com.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.chat.anntation.PassToken;
import com.chat.anntation.UserLoginToken;
import com.chat.entity.User;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import com.chat.util.JwtUtil;
import com.chat.util.RedisUtil;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
	@Autowired
	UserService userService;

	@Autowired
	RedisUtil redisUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String auth = request.getHeader("Authorization");
		if(CommondUtil.judgeStringEmpty(auth)) {
			response.setStatus(401);
			return false;
		}

		String token = auth.split(" ")[1];

		if(!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		if(method.isAnnotationPresent(PassToken.class)) {
			PassToken passToken = method.getAnnotation(PassToken.class);
			if(passToken.required()) {
				return true;
			}
		}

		ServletInputStream inputStream = request.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		if(method.isAnnotationPresent(UserLoginToken.class)) {
			UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
			if(userLoginToken.required()) {
				if(token == null) {
					response.setStatus(401);
					return false;
				}

				String line;
				StringBuilder sb = new StringBuilder();
				while((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}

				JSONObject json = JSONObject.parseObject(sb.toString());
				String userId = JwtUtil.parserJWT(token).getSubject();
				request.setAttribute("requestJson", json);
				if(!userId.equals(json.getString("userId"))) {
					response.setStatus(401);
					return false;
				}

				if(!Objects.equals(redisUtil.getMapString("token", userId), token)) {
					response.setStatus(401);
					return false;
				}

				User user = userService.getUserById(userId);
				if (user == null) {
					response.setStatus(401);
					return false;
				}

				return true;
			}
		}
		return true;
	}
}
