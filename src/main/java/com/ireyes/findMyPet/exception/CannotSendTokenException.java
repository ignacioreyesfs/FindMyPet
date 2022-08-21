package com.ireyes.findMyPet.exception;

public class CannotSendTokenException extends RuntimeException{
	public CannotSendTokenException(String contact, Throwable e) {
		super("Cannot send token to " + contact, e);
	}
}
