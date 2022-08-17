package com.ireyes.findMyPet.service.user;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.ireyes.findMyPet.model.user.Contact;
import com.ireyes.findMyPet.validation.ValidUserContacts;

public class UserDto {
	@NotEmpty
	private String username;
	private String firstName;
	@ValidUserContacts
	private List<Contact> contacts;
	
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
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
}
