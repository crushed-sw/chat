package com.chat.config;

import com.chat.controller.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getAuthenticationInterceptor())
				.addPathPatterns("/chat/**");
	}

	@Bean
	public AuthenticationInterceptor getAuthenticationInterceptor() {
		return new AuthenticationInterceptor();
	}
}
