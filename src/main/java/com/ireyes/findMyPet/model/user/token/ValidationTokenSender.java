package com.ireyes.findMyPet.model.user.token;

import com.ireyes.findMyPet.exception.CannotSendTokenException;

public interface ValidationTokenSender {
	void sendValidationToken(String contact, String token) throws CannotSendTokenException;
	
	void sendValidationTokenAync(String contact, String token);
}
