package com.ireyes.findMyPet.service.post;

import java.sql.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.ireyes.findMyPet.model.pet.Pet;
import com.ireyes.findMyPet.model.post.Location;
import com.ireyes.findMyPet.model.post.RelocationUrgency;
import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.validation.NotNullIfAnotherFieldHasValue;

@NotNullIfAnotherFieldHasValue(fieldName = "postType", fieldValue = "found", dependFieldName = "relocationUrgency")
public class PostDTO {
	@NotBlank
	@Pattern(regexp = "search|found")
	private String postType;
	@NotNull
	private Pet pet;
	private String description;
	@NotNull
	private Location location;
	@NotNull
	private User user;
	@NotNull
	private Date date;
	private RelocationUrgency relocationUrgency;
	@NotNull
	private MultipartFile image;
	
	// GETTERS AND SETTERS
	
	public String getPostType() {
		return postType;
	}
	public void setPostType(String postType) {
		this.postType = postType;
	}
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public RelocationUrgency getRelocationUrgency() {
		return relocationUrgency;
	}
	public void setRelocationUrgency(RelocationUrgency relocationUrgency) {
		this.relocationUrgency = relocationUrgency;
	}
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
}
