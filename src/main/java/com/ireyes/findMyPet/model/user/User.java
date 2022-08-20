package com.ireyes.findMyPet.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String firstName;
	private String email;
	private boolean enabled;
	
	@OneToMany(cascade = {CascadeType.ALL},
			fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name="user_id")
	private List<Contact> alternativeContacts;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="users_roles", joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id"))
	private List<Role> roles;
	
	public User() {
		alternativeContacts = new ArrayList<>();
		enabled = false;
	}
	
	public void addAlternativeContact(Contact contact) {
		alternativeContacts.add(contact);
	}
	
	public void removeAlternativeContact(Contact contact) {
		alternativeContacts.remove(contact);
	}
	
	// GETTERS AND SETTERS
	
	public List<Contact> getAlternativeContacts() {
		return alternativeContacts;
	}

	public void setAlternativeContacts(List<Contact> contacts) {
		this.alternativeContacts.clear();
		this.alternativeContacts.addAll(contacts);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
