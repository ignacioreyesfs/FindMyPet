package com.ireyes.findMyPet.service.user;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterDTO {
	@NotEmpty(message="required")
	@Size(min = 3, max = 12, message="length between 3-12")
	@Pattern(regexp = "^[A-Za-z0-9]+$", message="only alphanumeric allowed")
	private String username;
	@Valid
	private PasswordDTO password;
	@NotEmpty(message="required")
	@Email(message="invalid email")
	private String email;
	
	// GETTERS AND SETTERS
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public PasswordDTO getPassword() {
		return password;
	}
	public void setPassword(PasswordDTO password) {
		this.password = password;
	}

}
