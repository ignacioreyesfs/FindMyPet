package com.ireyes.findMyPet.service.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {
	@NotEmpty(message="required")
	@Size(min = 3, max = 12, message="length between 3-12")
	@Pattern(regexp = "^[A-Za-z0-9]+$", message="only alphanumeric allowed")
	private String username;
	@NotEmpty(message="required")
	@Size(min = 8, max = 16, message="length between 8-16")
	@Pattern(regexp = "[A-Z].*[0-9]", message="alphanumeric starting with capital and ending with a number")
	private String password;
	@NotEmpty(message="required")
	@Size(min = 8, max = 16, message="length between 8-16")
	@Pattern(regexp = "[A-Z].*[0-9]", message="alphanumeric starting with capital and ending with a number")
	private String confirmPassword;
	@NotEmpty(message="required")
	@Email(message="invalid email")
	private String email;
	
	public boolean passwordsMatches() {
		return password.equals(confirmPassword);
	}
	
	// GETTERS AND SETTERS
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
