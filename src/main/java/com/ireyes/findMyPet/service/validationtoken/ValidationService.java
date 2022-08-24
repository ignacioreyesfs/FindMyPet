package com.ireyes.findMyPet.service.validationtoken;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.ireyes.findMyPet.controller.ResourceNotFoundException;
import com.ireyes.findMyPet.dao.UserRepository;
import com.ireyes.findMyPet.dao.ValidationTokenRepository;
import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.model.user.register.OnValidationTokenExpire;
import com.ireyes.findMyPet.model.user.register.ValidationToken;
import com.ireyes.findMyPet.model.user.register.ValidationTokenSender;
import com.ireyes.findMyPet.service.user.InvalidTokenException;

@Service
public class ValidationService {
	@Autowired
	private ValidationTokenRepository validationTokenRepo;
	@Autowired
	private ValidationTokenSender validationTokenSender;
	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserRepository userRepo;
	
	@Transactional
	public ValidationToken createValidationToken(User user) {
		ValidationToken token = new ValidationToken();
		OnValidationTokenExpire onValidationTokenExpire = context.getBean(OnValidationTokenExpire.class);
		
		token.setUser(user);
		token.setToken(UUID.randomUUID().toString());
		token.setExpirationTime(60);
		
		validationTokenRepo.deleteByUser(user);
		token = validationTokenRepo.save(token);
		
		onValidationTokenExpire.setId(token.getId());
		taskScheduler.schedule(onValidationTokenExpire, new Date(token.getExpirationDate()));
		
		return token;
	}
	
	@Transactional
	public void validateAccount(String token) {
		ValidationToken validationToken = validationTokenRepo.findByToken(token);
		User user;
		
		if(validationToken == null || validationToken.getExpirationDate() < Calendar.getInstance().getTimeInMillis()) {
			throw new InvalidTokenException();
		}
		
		user = validationToken.getUser();
		user.setEnabled(true);
		
		userRepo.save(user);		
		validationTokenRepo.deleteById(validationToken.getId());
	}
	
	@Transactional
	public void resendAccountConfirmationToken(String email) throws ResourceNotFoundException, UserAlreadyEnabledException{
		User user = userRepo.findByEmail(email).orElseThrow(ResourceNotFoundException::new);
		
		if(user.isEnabled()) {
			throw new UserAlreadyEnabledException();
		}
		
		ValidationToken token = validationTokenRepo.findByUser(user);
		
		if(token == null || token.getExpirationDate() <= Calendar.getInstance().getTimeInMillis()) {
			token = createValidationToken(user);
		}
		
		validationTokenSender.sendValidationTokenAync(email, token.getToken());
	}
	
}
