package com.ireyes.findMyPet.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.service.user.PasswordDTO;
import com.ireyes.findMyPet.service.user.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/user/profile")
	public String showProfile() {
		return "user-profile";
	}
	
	@GetMapping("/user/profile/edit")
	public String showEdit(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName()).orElseThrow();
		
		model.addAttribute("user", user);
		
	    if (!model.containsAttribute("newPassword")) {
	        model.addAttribute("newPassword", new PasswordDTO());
	    }
		
		return "user-edit";
	}
	
	@PostMapping("/user/profile/edit/password")
	public String changePassword(@RequestParam String actualPassword, @Valid @ModelAttribute PasswordDTO newPassword,
			BindingResult br, RedirectAttributes redirect, Principal principal) {
		User user = userService.findByUsername(principal.getName()).orElseThrow();
		
		redirect.addFlashAttribute("user", user);
		
		if(!userService.userPasswordMatch(principal.getName(), actualPassword)) {
			redirect.addFlashAttribute("passwordError", "Invalid actual password");
			redirect.addFlashAttribute("newPassword", new PasswordDTO());
			return "redirect:/user/profile/edit";
		}
		
		if(br.hasErrors()) {
			redirect.addFlashAttribute("org.springframework.validation.BindingResult.newPassword", br);
			redirect.addFlashAttribute("newPassword", newPassword);
			return "redirect:/user/profile/edit";
		}
		
		userService.changeUserPassword(principal.getName(), newPassword.getValue());
		
		redirect.addFlashAttribute("successMessage", "Password changed sucessfully");
		
		return "redirect:/user/profile/edit";
	}
}
