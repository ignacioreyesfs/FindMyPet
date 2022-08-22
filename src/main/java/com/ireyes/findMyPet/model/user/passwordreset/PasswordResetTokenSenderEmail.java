package com.ireyes.findMyPet.model.user.passwordreset;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ireyes.findMyPet.model.email.EmailService;

@Component
public class PasswordResetTokenSenderEmail implements PasswordResetTokenSender{
	@Autowired
	private EmailService emailService;
	@Autowired
	@Qualifier("passwordResetTemplate")
	private SimpleMailMessage template;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Override
	public void sendPasswordResetToken(String email, String token) throws CannotSendTokenException{
		sendToken(email, token);
	}

	@Override
	@Async
	public void sendPasswordResetTokenAsync(String email, String token) {
		try {
			sendToken(email, token);
		}catch (CannotSendTokenException e) {
			e.printStackTrace();
			logger.severe("Cannot send password reset email to " + email);
		}
		
	}
	
	private void sendToken(String email, String token) throws CannotSendTokenException {
		try {
			emailService.sendEmail(email, template.getSubject(), String.format(template.getText(), token));
		}catch (Exception e) {
			throw new CannotSendTokenException(email, e);
		}

	}

}
