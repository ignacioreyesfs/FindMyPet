package com.ireyes.findMyPet.model.user.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.service.user.UserService;

@Component
public class RegistrationListener {
	@Autowired
	private UserService userService;
	@Autowired
	private ValidationTokenSender tokenSender;
	
	@EventListener
	public void handleEvent(RegistrationCompleteEvent event) {
		User user = event.getUser();
		ValidationToken token = userService.createValidationToken(user);
		tokenSender.sendValidationTokenAync(user.getEmail(), token.getToken());
	}
}
