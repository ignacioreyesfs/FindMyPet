package com.ireyes.findMyPet.controller.auth;

import java.security.Principal;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.service.user.UserDTO;
import com.ireyes.findMyPet.service.user.UserService;

@Controller
public class RegistrationController {
	@Autowired
	private UserService userService;
	private String signUpForm = "sign-up";
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@GetMapping("/register")
	public String registerForm(Model model, Principal principal) {
		if(principal != null) {
			return "redirect:/";
		}
		
		model.addAttribute("user", new UserDTO());
		return signUpForm;
	}
	
	@PostMapping("/register")
	public String showRegister(@Valid @ModelAttribute("user") UserDTO userForm,
			BindingResult br, Model model, RedirectAttributes redirect) {
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
		
		redirect.addFlashAttribute("userCreated", true);
		return "redirect:/login";
	}
}
