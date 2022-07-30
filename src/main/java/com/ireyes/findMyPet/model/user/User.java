package com.ireyes.findMyPet.model.user;

import java.util.ArrayList;
import java.util.List;

public class User {
	List<Contact> contacts;
	
	public User() {
		contacts = new ArrayList<>();
	}
	
	public void addContact(Contact contact) {
		contacts.add(contact);
	}
	
	public void removeContact(Contact contact) {
		contacts.remove(contact);
	}
	
	// GETTERS AND SETTERS
	
	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
}
