package com.ireyes.findMyPet.service.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ireyes.findMyPet.dao.RoleRepository;
import com.ireyes.findMyPet.dao.UserRepository;
import com.ireyes.findMyPet.dao.ValidationTokenRepository;
import com.ireyes.findMyPet.exception.InvalidTokenException;
import com.ireyes.findMyPet.model.user.Contact;
import com.ireyes.findMyPet.model.user.Role;
import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.model.user.token.ValidationToken;

@Service
public class UserService implements UserDetailsService{
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private ValidationTokenRepository tokenRepo;
	@Autowired
	@Lazy
	private PasswordEncoder encoder;
	
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
		token.setUser(user);
		token.setToken(UUID.randomUUID().toString());
		token.setExpirationTime(60);
		
		tokenRepo.save(token);
		
		return token;
	}
	
	@Transactional
	public void validateAccount(String token) {
		ValidationToken validationToken = tokenRepo.findByToken(token);
		User user;
		
		if(validationToken == null || validationToken.getExpirationDate() < Calendar.getInstance().getTimeInMillis()) {
			throw new InvalidTokenException();
		}
		
		user = validationToken.getUser();
		user.setEnabled(true);
		
		userRepo.save(user);		
		tokenRepo.deleteById(validationToken.getId());
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
