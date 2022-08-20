package com.ireyes.findMyPet.model.user.token;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ireyes.findMyPet.model.email.EmailService;
import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.service.user.UserService;

@Component
public class RegistrationListener {
	@Autowired
	private EmailService emailService;
	@Autowired
	private UserService userService;
	@Autowired
	@Qualifier("registrationValidatorTemplate")
	private SimpleMailMessage template;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@EventListener
	@Async
	public void handleEvent(RegistrationCompleteEvent event) {
		User user = event.getUser();
		ValidationToken token = userService.createValidationToken(user);
		try {
			emailService.sendEmail(user.getEmail(), template.getSubject(), String.format(template.getText(), token.getToken()));
		} catch (Exception e) {
			logger.severe("Cannot send verification email email to " + user.getEmail());
			e.printStackTrace();
		}
	}
}
