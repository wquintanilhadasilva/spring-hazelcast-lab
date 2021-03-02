package com.example.demo.sardine;

import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {
	
	private String userName = "****";
	private String password = "***";
	private String url = "https://webdav.4shared.com";
	
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
