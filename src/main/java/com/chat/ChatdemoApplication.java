package com.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ChatdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatdemoApplication.class, args);
	}

}
