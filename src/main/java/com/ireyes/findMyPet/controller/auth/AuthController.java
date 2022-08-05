package com.ireyes.findMyPet.controller.auth;

import java.util.Optional;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.service.UserService;

@Controller
public class AuthController {
	@Autowired
	private UserService userService;
	private String signUpForm = "sign-up";
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("user", new UserForm());
		return signUpForm;
	}
	
	@PostMapping("/register")
	public String showRegister(@Valid @ModelAttribute("user") UserForm userForm,
			BindingResult br, Model model) {
		if(br.hasErrors()) {
			return signUpForm;
		}
		
		Optional<User> existing = userService.findByUsername(userForm.getUsername());
		if(existing.isPresent()) {
			model.addAttribute("user", userForm);
			model.addAttribute("registrationError", "User name already exists");
			return signUpForm;
		}
		
		if(!userForm.passwordsMatches()) {
			model.addAttribute("user", userForm);
			model.addAttribute("registrationError", "Passwords doesn't match");
			return signUpForm;
		}
		
		userService.save(userForm);
		logger.info("Created user " + userForm.getUsername());
		
		model.addAttribute("userCreated", true);
		return "redirect:/login";
	}
}
