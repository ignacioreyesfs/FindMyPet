package com.ireyes.findMyPet.controller;

public class ResourceNotFoundException extends RuntimeException{
	public ResourceNotFoundException() {
		super();
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	public ResourceNotFoundException(String message, Throwable e) {
		super(message, e);
	}
}
