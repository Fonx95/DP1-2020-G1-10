package org.springframework.samples.farmatic.model;

import lombok.Data;

@Data
public class UserValidate {
	
	private String username;
	
	private String password;
	
	private String newPassword;
	
	private String validPassword;
	
	public UserValidate() {
		
	}
	
	public UserValidate(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
