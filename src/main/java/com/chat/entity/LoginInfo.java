package com.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginInfo {
	private String userId;
	private String userName;
	private Integer status;

	public static LoginInfoBuilder builder() {
		return new LoginInfoBuilder();
	}

	public static class LoginInfoBuilder {
		private String userId;
		private String userName;
		private Integer status;

		public LoginInfoBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public LoginInfoBuilder userName(String userName) {
			this.userName = userName;
			return this;
		}

		public LoginInfoBuilder status(Integer status) {
			this.status = status;
			return this;
		}

		public LoginInfo build() {
			return new LoginInfo(userId, userName, status);
		}
	}
}
