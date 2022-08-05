package com.ireyes.findMyPet.service;

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

import com.ireyes.findMyPet.controller.auth.UserForm;
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
	public User save(User user) {
		return userRepo.save(user);
	}
	
	@Transactional
	public void save(UserForm userForm) {
		User user = new User();
		Role role = roleRepo.findByName("ROLE_USER");
		
		if(role == null) {
			role = roleRepo.save(new Role("ROLE_USER"));
		}
		
		user.setUsername(userForm.getUsername());
		user.setPassword(encoder.encode(userForm.getPassword()));
		user.addContact(new Contact(ContactType.EMAIL, userForm.getEmail()));
		user.setRoles(Arrays.asList(role));
		userRepo.save(user);
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
