package com.ireyes.findMyPet.service;

import java.util.Arrays;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ireyes.findMyPet.controller.auth.UserForm;
import com.ireyes.findMyPet.dao.UserRepository;
import com.ireyes.findMyPet.model.user.Contact;
import com.ireyes.findMyPet.model.user.ContactType;
import com.ireyes.findMyPet.model.user.Role;
import com.ireyes.findMyPet.model.user.User;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
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
		user.setUsername(userForm.getUsername());
		user.setPassword(encoder.encode(userForm.getPassword()));
		user.addContact(new Contact(ContactType.EMAIL, userForm.getEmail()));
		user.setRoles(Arrays.asList(new Role("ROLE_USER")));
		userRepo.save(user);
	}
}
