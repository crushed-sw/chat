package com.chat.util;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {
	public static final Long JWT_TTL = 24 * 60 * 60 * 1000L;
	public static final String JWT_KEY = "chat";

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", ".");
	}

	public static String createJWT(String subject) {
		JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
		return builder.compact();
	}

	public static String createJWT(String subject, Long ttlMillis) {
		JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
		return builder.compact();
	}

	private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		SecretKey secretKey = generalKey();
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		if(ttlMillis == null) {
			ttlMillis = JwtUtil.JWT_TTL;
		}
		Long expMillis = nowMillis + ttlMillis;
		Date expDate = new Date(expMillis);
		return Jwts.builder()
				.setId(uuid)
				.setSubject(subject)
				.setIssuer("chat")
				.setIssuedAt(now)
				.signWith(signatureAlgorithm, secretKey)
				.setExpiration(expDate);
	}

	public static String createJWT(String id, String subject, Long ttlMillis) {
		JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
		return builder.compact();
	}

	public static SecretKey generalKey() {
		byte[] encodeKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
		SecretKey key = new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
		return key;
	}

	public static Claims parserJWT(String jwt) throws Exception {
		SecretKey secretKey = generalKey();
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(jwt)
				.getBody();
	}
}
