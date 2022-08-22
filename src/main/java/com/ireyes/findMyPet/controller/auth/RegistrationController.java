package com.ireyes.findMyPet.controller.auth;

import java.security.Principal;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ireyes.findMyPet.controller.ResourceNotFoundException;
import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.model.user.register.RegistrationCompleteEvent;
import com.ireyes.findMyPet.service.user.EmailAlreadyExists;
import com.ireyes.findMyPet.service.user.InvalidTokenException;
import com.ireyes.findMyPet.service.user.RegisterDTO;
import com.ireyes.findMyPet.service.user.UserAlreadyEnabledException;
import com.ireyes.findMyPet.service.user.UserAlreadyExistsException;
import com.ireyes.findMyPet.service.user.UserService;

@Controller
public class RegistrationController {
	@Autowired
	private UserService userService;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
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
			BindingResult br, Model model) {
		User user;
		
		if(br.hasErrors()) {
			return signUpForm;
		}
		
		try {
			user = userService.register(userForm);
		}catch(UserAlreadyExistsException e) {
			model.addAttribute("user", userForm);
			model.addAttribute("registrationError", "User name already exists");
			return signUpForm;
		}catch(EmailAlreadyExists e) {
			model.addAttribute("user", userForm);
			model.addAttribute("registrationError", "Email already in use");
			return signUpForm;
		}
		
		eventPublisher.publishEvent(new RegistrationCompleteEvent(user));
		
		logger.info("Created user " + userForm.getUsername());
		
		return "redirect:/confirm-account";
	}
	
	@GetMapping("/confirm-account")
	public String showConfirmAccount() {
		return "confirm-account";
	}
	
	@PostMapping("/confirm-account")
	public String confirmAccount(@RequestParam String code, Model model, RedirectAttributes redirect) {
		try {
			userService.validateAccount(code);
		}catch(InvalidTokenException e) {
			model.addAttribute("errorMessage", "Invalid or expired token");
			return "confirm-account";
		}
		
		redirect.addFlashAttribute("successMessage", "User created");
		
		return "redirect:/login";
	}
	
	@GetMapping("/resend-confirmation")
	public String showResendConfirmation() {
		return "resend-confirmation";
	}
	
	@PostMapping("/resend-confirmation")
	public String resendConfirmation(@RequestParam String email, Model model, RedirectAttributes redirect) {
		try {
			userService.resendAccountConfirmationToken(email);
		}catch(ResourceNotFoundException e) {
			model.addAttribute("errorMessage", "Email does not correspond to an user");
			return "resend-confirmation";
		}catch(UserAlreadyEnabledException e) {
			model.addAttribute("errorMessage", "Email user is already active");
			return "resend-confirmation";
		}
		
		redirect.addFlashAttribute("successMessage", "Confirmation token resend");
		
		return "redirect:/confirm-account";
	}
}
