package com.ireyes.findMyPet.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ireyes.findMyPet.model.user.ContactType;
import com.ireyes.findMyPet.service.PasswordResetService;
import com.ireyes.findMyPet.service.post.PostService;
import com.ireyes.findMyPet.service.user.PasswordDTO;
import com.ireyes.findMyPet.service.user.UserDTO;
import com.ireyes.findMyPet.service.user.UserService;

@Controller
public class UserController {
	private static final String PASSWORD_ATTR = "newPassword";
	
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordResetService passwordResetService;
	@Autowired
	private PostService postService;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@GetMapping("/user")
	@Secured("ROLE_USER")
	public String showProfile(Model model, Principal principal) {
		model.addAttribute("user", userService.findByUsername(principal.getName()).orElseThrow());
		model.addAttribute("posts", postService.findByUsername(principal.getName()));
		return "user-profile";
	}
	
	@GetMapping("/user/edit/password")
	@Secured("ROLE_USER")
	public String showEditPassword(Principal principal, Model model) {
	    if (!model.containsAttribute(PASSWORD_ATTR)) {
	        model.addAttribute(PASSWORD_ATTR, new PasswordDTO());
	    }
		
		return "user-edit-password";
	}
	
	@PostMapping("/user/edit/password")
	@Secured("ROLE_USER")
	public String changePassword(@RequestParam String actualPassword, @Valid @ModelAttribute PasswordDTO newPassword,
			BindingResult br, RedirectAttributes redirect, Principal principal) {
		
		if(!userService.userPasswordMatch(principal.getName(), actualPassword)) {
			redirect.addFlashAttribute("passwordError", "Invalid actual password");
			redirect.addFlashAttribute(PASSWORD_ATTR, new PasswordDTO());
			return "redirect:/user/edit/password";
		}
		
		if(br.hasErrors()) {
			redirect.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "newPassword", br);
			redirect.addFlashAttribute(PASSWORD_ATTR, newPassword);
			return "redirect:/user/edit/password";
		}
		
		userService.changeUserPassword(principal.getName(), newPassword.getValue());
		
		redirect.addFlashAttribute("successMessage", "Password changed sucessfully");
		
		return "redirect:/user";
	}
	
	@GetMapping("/user/edit/info")
	@Secured("ROLE_USER")
	public String showEditInfo(Model model, Principal principal) {
		UserDTO user = userService.findDtoByUsername(principal.getName()).orElseThrow();
		
		model.addAttribute("user", user);
		model.addAttribute("contactTypes", Arrays.asList(ContactType.values()));
		
		return "user-edit";
	}
	
	@PostMapping("/user/edit/info")
	@Secured("ROLE_USER")
	public String updateUserInfo(@Valid @ModelAttribute UserDTO user, BindingResult bindingResult, Model model, Principal principal) {
		if(!user.getUsername().equals(principal.getName())) {
			throw new AccessDeniedException("User is not " + user.getUsername());
		}
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			model.addAttribute("contactTypes", Arrays.asList(ContactType.values()));
			model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "user", bindingResult);
			return "user-edit";
		}
		
		userService.save(user);
		return "redirect:/user";
	}
	
	@PostMapping("/user/edit/addContact")
	@Secured("ROLE_USER")
	public String addContact(@ModelAttribute UserDTO user, Model model) {
		userService.addContact(user);
		model.addAttribute("contactTypes", Arrays.asList(ContactType.values()));
		model.addAttribute("user", user);
		return "user-edit :: contacts";
	}
	
	@PostMapping("/user/edit/removeContact")
	@Secured("ROLE_USER")
	public String removeContact(@ModelAttribute UserDTO user, @RequestParam("row") Integer contactIndex, Model model) {
		userService.removeContact(user, contactIndex.intValue());
		model.addAttribute("contactTypes", Arrays.asList(ContactType.values()));
		model.addAttribute("user", user);
		return "user-edit :: contacts";
	}
	
	@GetMapping("/password-reset")
	public String showResetPassword() {
		return "send-password-reset";
	}
	
	@PostMapping("/password-reset")
	public String sendPasswordReset(@RequestParam String email, Model model) {
		try {
			passwordResetService.sendResetPasswordToken(email);
		}catch(ResourceNotFoundException e) {
			model.addAttribute("errorMessage", "Email does not correspond to an user");
			return "send-password-reset";
		}
		
		return "redirect:/change-password";
	}
	
	@GetMapping("/change-password")
	public String changePassword(Model model) {
		model.addAttribute("newPassword", new PasswordDTO());
		return "password-reset";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam String code, @Valid @ModelAttribute PasswordDTO newPassword,
			BindingResult br, Model model, RedirectAttributes redirect) {
		
		if(br.hasErrors()) {
			model.addAttribute("code", code);
			model.addAttribute("newPassword", newPassword);
			model.addAttribute(BindingResult.MODEL_KEY_PREFIX+"newPassword", br);
			return "password-reset";
		}
		
		try {
			passwordResetService.resetPassword(code, newPassword.getValue());
		}catch(ResourceNotFoundException e) {
			model.addAttribute("newPassword", newPassword);
			model.addAttribute("errorMessage", "Invalid or expired token");
			return "password-reset";
		}
		
		redirect.addFlashAttribute("successMessage", "Password changed");
		
		return "redirect:/login";
	}
	
	@PostMapping("/user/delete")
	@Secured("ROLE_USER")
	public String deleteAccount(Principal principal) {
		userService.delete(principal.getName());
		logger.info("Deleted user " + principal.getName());
		SecurityContextHolder.getContext().setAuthentication(null);
		return "redirect:/";
	}
	
}
