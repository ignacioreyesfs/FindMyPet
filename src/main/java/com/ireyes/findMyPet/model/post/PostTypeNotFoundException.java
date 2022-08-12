package com.ireyes.findMyPet.model.post;

public class PostTypeNotFoundException extends RuntimeException{
	public PostTypeNotFoundException(String type) {
		super("Type not found " + type);
	}
}
