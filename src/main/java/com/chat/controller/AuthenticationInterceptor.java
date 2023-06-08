package com.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.chat.anntation.PassToken;
import com.chat.anntation.UserLoginToken;
import com.chat.entity.User;
import com.chat.service.UserService;
import com.chat.util.CommondUtil;
import com.chat.util.JwtUtil;
import com.chat.util.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * 拦截器类
 */
@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
	@Autowired
	UserService userService;

	@Autowired
	RedisUtil redisUtil;

	/**
	 * 在执请求之前验证token是否正确
	 * @param request HttpServletRequest对象
	 * @param response HttpServletResponse对象
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

		 // 判断是否是函数处理器
		if(!(handler instanceof HandlerMethod handlerMethod)) {
			return true;
		}

		// 判断是否带Authorization
		String auth = request.getHeader("Authorization");
		if(CommondUtil.judgeStringEmpty(auth)) {
			response.setStatus(401);
			log.error("[token] 该请求Authorization头不存在");
			return false;
		}

		// 判断是否符合自定义的格式
		String[] s = auth.split(" ");
		String token;
		String userId;
		try {
			token = s[1];
			userId = s[2];
		} catch (Exception e) {
			response.setStatus(401);
			log.error("[token] 该请求Authorization格式错误");
			return false;
		}

		Method method = handlerMethod.getMethod();

		// 将PassToken注解的函数直接放行
		if(method.isAnnotationPresent(PassToken.class)) {
			PassToken passToken = method.getAnnotation(PassToken.class);
			if(passToken.required()) {
				return true;
			}
		}

		// 验证token
		if(method.isAnnotationPresent(UserLoginToken.class)) {
			UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
			if(userLoginToken.required()) {
				// token和userId是否解析成功
				if(CommondUtil.judgeStringEmpty(token) || CommondUtil.judgeStringEmpty(userId)) {
					response.setStatus(401);
					log.error("[token] 该请求Authorization内容错误");
					return false;
				}

				// token是否能解析
				String tokenUserId;
				Claims claims;
				try {
					claims = JwtUtil.parserJWT(token);
					tokenUserId = claims.getSubject();
				} catch (Exception e) {
					response.setStatus(401);
					log.error("[token] " + userId + "请求token解析错误");
					return false;
				}

				// token和userId是否匹配
				if(!userId.equals(tokenUserId)) {
					response.setStatus(401);
					log.error("[token] " + userId + "该请求token和userId不匹配");
					return false;
				}
				request.setAttribute("userId", userId);

				// token是否被更新
				if(!Objects.equals(redisUtil.getMapString("token", userId), token)) {
					response.setStatus(401);
					log.error("[token] " + userId + "该请求token被更新");
					return false;
				}

				// token是否过期
				if(claims.getIssuedAt().after(new Date(System.currentTimeMillis()))) {
					response.setStatus(401);
					log.error("[token] " + userId + "该请求token已过期");
					return false;
				}

				// userId是否存在
				User user = userService.getUserById(userId);
				if (user == null) {
					log.error("[token] " + userId + "该请求用户不存在");
					response.setStatus(401);
					return false;
				}

				return true;
			}
		}
		return true;
	}
}
