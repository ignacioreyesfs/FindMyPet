package com.ireyes.findMyPet.service.user;

public class UserAlreadyExistsException extends RuntimeException {
	public UserAlreadyExistsException(String username) {
		super("User " + username +" already exists");
	}
}
