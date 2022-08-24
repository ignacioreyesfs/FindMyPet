package com.ireyes.findMyPet.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
	@GetMapping("/admin")
	@Secured("ROLE_ADMIN")
	public String showAdminPanel() {
		return "admin";
	}
}
