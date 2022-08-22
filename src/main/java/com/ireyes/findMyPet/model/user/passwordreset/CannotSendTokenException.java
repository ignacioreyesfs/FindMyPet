package com.ireyes.findMyPet.model.user.passwordreset;

public class CannotSendTokenException extends RuntimeException{
	public CannotSendTokenException(String contact, Throwable e) {
		super("Cannot send token to " + contact, e);
	}
}
