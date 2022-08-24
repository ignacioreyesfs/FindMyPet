package com.ireyes.findMyPet.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ireyes.findMyPet.controller.ResourceNotFoundException;
import com.ireyes.findMyPet.dao.PasswordResetTokenRepo;
import com.ireyes.findMyPet.dao.UserRepository;
import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.model.user.passwordreset.OnPasswordResetTokenExpire;
import com.ireyes.findMyPet.model.user.passwordreset.PasswordResetToken;
import com.ireyes.findMyPet.model.user.passwordreset.PasswordResetTokenSender;

@Service
public class PasswordResetService {
	@Autowired
	@Lazy
	private PasswordEncoder encoder;
	@Autowired
	private PasswordResetTokenSender passwordResetTokenSender;
	@Autowired
	private PasswordResetTokenRepo passwordResetTokenRepo;
	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserRepository userRepo;
	
	@Transactional
	public void sendResetPasswordToken(String email) throws ResourceNotFoundException{
		User user = userRepo.findByEmail(email).orElseThrow(ResourceNotFoundException::new);
		PasswordResetToken token = passwordResetTokenRepo.findByUser(user);
		
		if(token == null || token.getExpirationDate() <= Calendar.getInstance().getTimeInMillis()) {
			token = createPasswordResetToken(user);
		}
		
		passwordResetTokenSender.sendPasswordResetTokenAsync(email, token.getToken());		
	}
	
	@Transactional
	private PasswordResetToken createPasswordResetToken(User user) {
		PasswordResetToken token = new PasswordResetToken();
		OnPasswordResetTokenExpire onPasswordResetTokenExpire = context.getBean(OnPasswordResetTokenExpire.class);
		
		token.setUser(user);
		token.setToken(UUID.randomUUID().toString());
		token.setExpirationTime(10);
		
		passwordResetTokenRepo.deleteByUser(user);
		token = passwordResetTokenRepo.save(token);
		
		onPasswordResetTokenExpire.setId(token.getId());
		taskScheduler.schedule(onPasswordResetTokenExpire, new Date(token.getExpirationDate()));
		
		return token;
	}
	
	@Transactional
	public void resetPassword(String token, String newPassword) throws ResourceNotFoundException{
		PasswordResetToken pwResetToken = passwordResetTokenRepo.findByToken(token);
		if(pwResetToken == null || pwResetToken.getExpirationDate() <= Calendar.getInstance().getTimeInMillis()) {
			throw new ResourceNotFoundException();
		}
		
		User user = pwResetToken.getUser();
				
		user.setPassword(encoder.encode(newPassword));
		userRepo.save(user);
		passwordResetTokenRepo.delete(pwResetToken);
	}
}
