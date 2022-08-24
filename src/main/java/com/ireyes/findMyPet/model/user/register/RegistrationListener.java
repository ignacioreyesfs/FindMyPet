package com.ireyes.findMyPet.model.user.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.service.validationtoken.ValidationService;

@Component
public class RegistrationListener {
	private ValidationTokenSender tokenSender;
	private ValidationService validationService;
	
	@Autowired
	public RegistrationListener(ValidationTokenSender tokenSender, ValidationService validationService) {
		this.tokenSender = tokenSender;
		this.validationService = validationService;
	}
	
	@EventListener
	public void handleEvent(RegistrationCompleteEvent event) {
		User user = event.getUser();
		ValidationToken token = validationService.createValidationToken(user);
		tokenSender.sendValidationTokenAync(user.getEmail(), token.getToken());
	}
}
