package com.ireyes.findMyPet.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.service.UserService;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler{
	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		String username = auth.getName();
		User user = userService.findByUsername(username).get();
		
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		
		response.sendRedirect(request.getContextPath() + "/");
	}
	
}
