package com.ireyes.findMyPet.controller.auth;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String showLogin(HttpSession session) {
		if(session.getAttribute("user") != null) {
			return "redirect:/";
		}
		
		return "login";
	}
}
