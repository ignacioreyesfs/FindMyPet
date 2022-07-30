package com.ireyes.findMyPet.model.post;

import com.ireyes.findMyPet.model.pet.Pet;

public class Post {
	private Pet pet;
	private String description;
	private Location location;
	
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
}
