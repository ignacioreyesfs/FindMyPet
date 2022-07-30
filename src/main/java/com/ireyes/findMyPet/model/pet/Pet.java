package com.ireyes.findMyPet.model.pet;

public class Pet {
	private String name;
	private int age;
	private Breed breed;
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Breed getBreed() {
		return breed;
	}
	public void setBreed(Breed breed) {
		this.breed = breed;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
