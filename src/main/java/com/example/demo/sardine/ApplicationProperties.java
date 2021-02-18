package com.example.demo.sardine;

import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {
	
	private String userName = "user";
	private String password = "pwd";
	private String url = "http://www.urlwebdav.com.br/app";
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
