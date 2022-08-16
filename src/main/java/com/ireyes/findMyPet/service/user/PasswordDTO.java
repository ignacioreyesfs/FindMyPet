package com.ireyes.findMyPet.service.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ireyes.findMyPet.validation.FieldMatch;

@FieldMatch(first = "value", second = "confirmValue", message = "password not match")
public class PasswordDTO {
	@NotBlank(message="required")
	@Size(min = 8, max = 16, message="length between 8-16")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}$", 
		message="8-16 characters, at least one uppercase letter and one number:")
	private String value;
	private String confirmValue;
	
	// GETTERS AND SETTERS
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getConfirmValue() {
		return confirmValue;
	}
	public void setConfirmValue(String confirmValue) {
		this.confirmValue = confirmValue;
	}
}
