package com.ireyes.findMyPet.service.user;

public class EmailAlreadyExists extends RuntimeException{
	public EmailAlreadyExists(String email) {
		super("Email already exists: " + email);
	}
}
