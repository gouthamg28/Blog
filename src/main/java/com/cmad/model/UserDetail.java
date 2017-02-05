package com.cmad.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class UserDetail {

	@Id
	private String id = new ObjectId().toString();
	
	private String username;
	private String pwd;
	private String fullName;
	private String phno;
	private String areaofinterest;
	
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullname) {
		this.fullName = fullname;
	}
	public String getPhno() {
		return phno;
	}
	public void setPhno(String phno) {
		this.phno = phno;
	}
	public String getAreaofinterest() {
		return areaofinterest;
	}
	public void setAreaofinterest(String areaofinterest) {
		this.areaofinterest = areaofinterest;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "UserDetail  username: "+username+", pwd: "+pwd+", fullname: "+fullName+", phno: "+phno+" "+", areaofinterest: "+areaofinterest;
	}
	
}
