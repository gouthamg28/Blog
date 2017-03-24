package com.cmad.model.dto;

public class AuthData {
	String userId;
	String userToken;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String toString() {
		return "userId:" + userId + "userToken:" + userToken;
	}
}
