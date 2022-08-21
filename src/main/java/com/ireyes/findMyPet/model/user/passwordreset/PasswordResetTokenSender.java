package com.ireyes.findMyPet.model.user.passwordreset;

import com.ireyes.findMyPet.exception.CannotSendTokenException;

public interface PasswordResetTokenSender {
	void sendPasswordResetToken(String contact, String token) throws CannotSendTokenException;
	
	void sendPasswordResetTokenAsync(String contact, String token);
}
