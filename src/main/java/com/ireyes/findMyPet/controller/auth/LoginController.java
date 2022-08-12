package com.ireyes.findMyPet.controller.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String showLogin(HttpServletRequest request) {
		if(request.getUserPrincipal() != null) {
			return "redirect:/";
		}
		
		request.getSession().setAttribute("url_prior_login", request.getHeader("Referer"));
		
		return "login";
	}
}
