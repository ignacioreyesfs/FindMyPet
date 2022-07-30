package com.ireyes.findMyPet.model.post;

import java.util.List;

import com.ireyes.findMyPet.model.pet.Pet;
import com.ireyes.findMyPet.model.user.Contact;
import com.ireyes.findMyPet.model.user.User;

public class Post {
	private Pet pet;
	private String description;
	private Location location;
	private User user;
	private List<Contact> alternativeContacts;
	
	// GETTERS AND SETTERS
	
	public Pet getPet() {
		return pet;
	}
	public void setPet(Pet pet) {
		this.pet = pet;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Contact> getAlternativeContacts() {
		return alternativeContacts;
	}
	public void setAlternativeContacts(List<Contact> alternativeContacts) {
		this.alternativeContacts = alternativeContacts;
	}
}
