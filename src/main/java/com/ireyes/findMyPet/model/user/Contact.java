package com.ireyes.findMyPet.model.user;

public class Contact {
	private ContactType type;
	private String value;
	
	// GETTERS AND SETTERS
	
	public ContactType getType() {
		return type;
	}
	public void setType(ContactType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
