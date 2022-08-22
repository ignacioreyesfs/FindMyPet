package com.ireyes.findMyPet.service.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ireyes.findMyPet.controller.ResourceNotFoundException;
import com.ireyes.findMyPet.dao.PasswordResetTokenRepo;
import com.ireyes.findMyPet.dao.RoleRepository;
import com.ireyes.findMyPet.dao.UserRepository;
import com.ireyes.findMyPet.dao.ValidationTokenRepository;
import com.ireyes.findMyPet.exception.InvalidTokenException;
import com.ireyes.findMyPet.exception.UserAlreadyEnabledException;
import com.ireyes.findMyPet.model.user.Contact;
import com.ireyes.findMyPet.model.user.Role;
import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.model.user.passwordreset.OnPasswordResetTokenExpire;
import com.ireyes.findMyPet.model.user.passwordreset.PasswordResetToken;
import com.ireyes.findMyPet.model.user.passwordreset.PasswordResetTokenSender;
import com.ireyes.findMyPet.model.user.register.OnValidationTokenExpire;
import com.ireyes.findMyPet.model.user.register.ValidationToken;
import com.ireyes.findMyPet.model.user.register.ValidationTokenSender;

@Service
public class UserService implements UserDetailsService{
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private ValidationTokenRepository validationTokenRepo;
	@Autowired
	@Lazy
	private PasswordEncoder encoder;
	@Autowired
	private ValidationTokenSender validationTokenSender;
	@Autowired
	private PasswordResetTokenSender passwordResetTokenSender;
	@Autowired
	private PasswordResetTokenRepo passwordResetTokenRepo;
	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;
	@Autowired
	private ApplicationContext context;
	
	@Transactional
	public Optional<User> findByUsername(String username){
		return userRepo.findByUsername(username);
	}
	
	@Transactional
	public Optional<UserDto> findDtoByUsername(String username){
		Optional<User> user = userRepo.findByUsername(username);
		if(user.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(getUserDto(user.get()));
	}
	
	@Transactional
	public void save(UserDto userDto) {
		User user = userRepo.findByUsername(userDto.getUsername()).orElseThrow();
		user.setAlternativeContacts(userDto.getAlternativeContacts());
		user.setFirstName(userDto.getFirstName().trim());
		userRepo.save(user);
	}
	
	@Transactional
	public User register(RegisterDTO userForm) throws UserAlreadyExistsException, EmailAlreadyExists{
		User user = new User();
		
		if(userRepo.existsByUsername(userForm.getUsername())) {
			throw new UserAlreadyExistsException(userForm.getUsername());
		}
		
		if(userRepo.existsByEmail(userForm.getEmail())){
			throw new EmailAlreadyExists(userForm.getEmail());
		}
		
		Role role = roleRepo.findByName("ROLE_USER");
		
		if(role == null) {
			role = roleRepo.save(new Role("ROLE_USER"));
		}
		
		user.setUsername(userForm.getUsername());
		user.setPassword(encoder.encode(userForm.getPassword().getValue()));
		user.setEmail(userForm.getEmail());
		user.setRoles(Arrays.asList(role));
		
		return userRepo.save(user);
	}
	
	@Transactional
	public void changeUserPassword(String username, String newPassword) {
		User user = userRepo.findByUsername(username).orElseThrow();
		user.setPassword(encoder.encode(newPassword));
		userRepo.save(user);
	}
	
	public boolean userPasswordMatch(String username, String password) {
		User user = userRepo.findByUsername(username).orElseThrow();
		
		return encoder.matches(password, user.getPassword());
	}
	
	private UserDto getUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setAlternativeContacts(user.getAlternativeContacts());
		userDto.setUsername(user.getUsername());
		userDto.setFirstName(user.getFirstName());
		userDto.setEmail(user.getEmail());
		
		return userDto;
	}
	
	public void addContact(UserDto user) {
		if(user.getAlternativeContacts() == null) {
			user.setAlternativeContacts(new ArrayList<>());
		}
		user.getAlternativeContacts().add(new Contact());
	}
	
	public void removeContact(UserDto user, int index) {
		user.getAlternativeContacts().remove(index);
	}
	
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

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username does not exists"));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), 
				user.getPassword(), user.isEnabled(), true, true, true, mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}
