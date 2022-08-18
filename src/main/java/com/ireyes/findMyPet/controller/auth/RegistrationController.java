package com.ireyes.findMyPet.controller.auth;

import java.security.Principal;
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

import com.ireyes.findMyPet.service.user.RegisterDTO;
import com.ireyes.findMyPet.service.user.UserAlreadyExistsException;
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
		
		model.addAttribute("user", new RegisterDTO());
		return signUpForm;
	}
	
	@PostMapping("/register")
	public String showRegister(@Valid @ModelAttribute("user") RegisterDTO userForm,
			BindingResult br, Model model, RedirectAttributes redirect) {
		if(br.hasErrors()) {
			return signUpForm;
		}
		
		try {
			userService.register(userForm);
		}catch(UserAlreadyExistsException e) {
			model.addAttribute("user", userForm);
			model.addAttribute("registrationError", "User name already exists");
			return signUpForm;
		}
		
		logger.info("Created user " + userForm.getUsername());
		
		redirect.addFlashAttribute("userCreated", true);
		return "redirect:/login";
	}
}
