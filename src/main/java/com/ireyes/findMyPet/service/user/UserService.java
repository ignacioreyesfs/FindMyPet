package com.ireyes.findMyPet.service.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
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
import com.ireyes.findMyPet.model.user.Contact;
import com.ireyes.findMyPet.model.user.ContactType;
import com.ireyes.findMyPet.model.user.Role;
import com.ireyes.findMyPet.model.user.User;

@Service
public class UserService implements UserDetailsService{
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
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
		user.setContacts(userDto.getContacts());
		user.setFirstName(userDto.getFirstName().trim());
		userRepo.save(user);
	}
	
	@Transactional
	public void register(RegisterDTO userForm) throws UserAlreadyExistsException{
		User user = new User();
		
		if(userRepo.existsByUsername(userForm.getUsername())) {
			throw new UserAlreadyExistsException(userForm.getUsername());
		}
		
		Role role = roleRepo.findByName("ROLE_USER");
		
		if(role == null) {
			role = roleRepo.save(new Role("ROLE_USER"));
		}
		
		user.setUsername(userForm.getUsername());
		user.setPassword(encoder.encode(userForm.getPassword().getValue()));
		user.addContact(new Contact(ContactType.EMAIL, userForm.getEmail()));
		user.setRoles(Arrays.asList(role));
		userRepo.save(user);
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
		userDto.setContacts(user.getContacts());
		userDto.setUsername(user.getUsername());
		userDto.setFirstName(user.getFirstName());
		
		return userDto;
	}
	
	public void addContact(UserDto user) {
		if(user.getContacts() == null) {
			user.setContacts(new ArrayList<>());
		}
		user.getContacts().add(new Contact());
	}
	
	public void removeContact(UserDto user, int index) {
		user.getContacts().remove(index);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByUsername(username);
		if(!user.isPresent()) {
			throw new UsernameNotFoundException("Username does not exists");
		}
		return new org.springframework.security.core.userdetails.User(user.get().getUsername(), 
				user.get().getPassword(), mapRolesToAuthorities(user.get().getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}
