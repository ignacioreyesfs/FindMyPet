package com.ireyes.findMyPet.model.pet;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pet")
public class Pet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private int age;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="breed_id")
	private Breed breed;
	private String description;
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name="pet_images", joinColumns = @JoinColumn(name="pet_id"), 
		foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (pet_id) REFERENCES pet(id) ON DELETE CASCADE"))
	@Column(name="image")
	private List<String> imagesFilenames;
	
	public Pet() {
		imagesFilenames = new ArrayList<>();
	}
	public Pet(String name, int age, Breed breed, String description) {
		this();
		this.name = name;
		this.age = age;
		this.breed = breed;
		this.description = description;
	}
	
	public void addImageFilename(String filename) {
		imagesFilenames.add(filename);
	}
	
	public void removeImageFilename(String filename) {
		imagesFilenames.remove(filename);
	}
	
	// GETTERS AND SETTERS
	
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
	public Long getId() {
		return id;
	}
	public List<String> getImagesFilenames() {
		return imagesFilenames;
	}
	public void setImagesFilenames(List<String> imagesFilenames) {
		this.imagesFilenames = imagesFilenames;
	}
}
