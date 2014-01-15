package com.bxy.salestrategies.model;

public class User {
	private int id;
	private String Name;
	private String Login_name;
	private String password;
	
	public String getLogin_name() {
		return Login_name;
	}
	public void setLogin_name(String login_name) {
		Login_name = login_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
}
