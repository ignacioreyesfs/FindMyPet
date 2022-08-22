package com.ireyes.findMyPet.model.user.register;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ireyes.findMyPet.model.email.EmailService;
import com.ireyes.findMyPet.model.user.passwordreset.CannotSendTokenException;

@Component
public class ValidationTokenSenderEmail implements ValidationTokenSender{
	@Autowired
	private EmailService emailService;
	@Autowired
	@Qualifier("registrationValidatorTemplate")
	private SimpleMailMessage template;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void sendValidationToken(String contact, String token) throws CannotSendTokenException{
		sendToken(token, contact);
	}

	@Override
	@Async
	public void sendValidationTokenAync(String contact, String token) {
		try {
			sendToken(token, contact);
		} catch (CannotSendTokenException e) {
			e.printStackTrace();
			logger.info("Failed to send email to " + contact);
		}
		
	}
	
	private void sendToken(String token, String contact) throws CannotSendTokenException{
		try {
			emailService.sendEmail(contact, template.getSubject(), String.format(template.getText(), token));
		}catch (Exception e) {
			throw new CannotSendTokenException(contact, e);
		}
	}
	
}
