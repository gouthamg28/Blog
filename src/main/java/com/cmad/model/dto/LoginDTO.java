package com.cmad.model.dto;

public class LoginDTO {

	private String username;
	private String pwd;
	private String token;
	private String id;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "LoginDTO  username: "+username+", pwd: "+pwd+", id: "+id+", token: "+token;
	}
}
