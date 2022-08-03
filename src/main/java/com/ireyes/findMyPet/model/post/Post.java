package com.ireyes.findMyPet.model.post;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ireyes.findMyPet.model.pet.Pet;
import com.ireyes.findMyPet.model.user.Contact;
import com.ireyes.findMyPet.model.user.User;

@Entity
@Table(name="post")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
public abstract class Post {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(cascade = {CascadeType.ALL})
	private Pet pet;
	private String description;
	@Embedded
	private Location location;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name="post_alternativeContacts",
				joinColumns = @JoinColumn(name="post_id"))
	@Column(name="contact")
	private List<Contact> alternativeContacts;
	@Temporal(TemporalType.DATE)
	private Date date;
	
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
