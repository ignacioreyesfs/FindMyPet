package com.ireyes.findMyPet.service.user;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.ireyes.findMyPet.model.user.Contact;
import com.ireyes.findMyPet.validation.ValidUserContacts;

public class UserDTO {
	@NotEmpty
	private String username;
	private String firstName;
	@NotEmpty
	@Email
	private String email;
	@ValidUserContacts
	private List<Contact> alternativeContacts;
	
	// GETTERS AND SETTERS
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public List<Contact> getAlternativeContacts() {
		return alternativeContacts;
	}
	public void setAlternativeContacts(List<Contact> contacts) {
		this.alternativeContacts = contacts;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
