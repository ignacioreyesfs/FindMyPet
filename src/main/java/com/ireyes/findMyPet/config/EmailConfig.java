package com.ireyes.findMyPet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig {
	@Bean(name = "postUpdateTemplateMessage")
	public SimpleMailMessage postUpdateTemplateMessage() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("Any updates on your search?");
		message.setText("Hi %s,\n\nDo you have any update on your searches?\n\n"
				+ "Please don't forget to delete your solved searches!\n\n"
				+ "Yours sincerely, FindMyDog.");
		return message;
	}
	
	@Bean(name="registrationValidatorTemplate")
	public SimpleMailMessage registrationValidatorTemplate() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("Account Validation");
		message.setText("Hi, \n\nUse the following code to confirm you FindMyPet account:\n\n"
				+ "%s\n\nIf you have not registered on our site, please ignore this email.\n\n"
				+ "Yours sincerely, FindMyDog.");
		
		return message;
	}
	
	@Bean(name="passwordResetTemplate")
	public SimpleMailMessage passwordResetTemplate() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("Password Reset");
		message.setText("Hi, \n\nUse the following code to reset your FindMyPet password:\n\n"
				+ "%s\n\nDo not share this code with anyone else, will expire in a few minutes.\n"
				+ "If you did not require a password reset, ignore this email.\n\n"
				+ "Yours sincerely, FindMyDog.");
		
		return message;
	}
}
