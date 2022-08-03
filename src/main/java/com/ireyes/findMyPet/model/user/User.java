package com.ireyes.findMyPet.model.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private List<Contact> contacts;
	private String firstName;
	
	public User() {
		contacts = new ArrayList<>();
	}
	
	public void addContact(Contact contact) {
		contacts.add(contact);
	}
	
	public void removeContact(Contact contact) {
		contacts.remove(contact);
	}
	
	public String getEmail() {
		Optional<Contact>email = contacts.stream().
				filter(contact -> contact.getType() == ContactType.EMAIL).findFirst();
		return email.isPresent()? email.get().getValue(): null;
	}
	
	// GETTERS AND SETTERS
	
	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
