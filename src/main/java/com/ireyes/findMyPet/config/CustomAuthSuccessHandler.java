package com.ireyes.findMyPet.config;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler{
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		String redirect = null;
		HttpSession session = request.getSession();
		if(session != null) {
			redirect = (String)session.getAttribute("url_prior_login");
			session.removeAttribute("url_prior_login");
		}
		
		Logger.getGlobal().info("last url: " + redirect);
		
		if(redirect == null || redirect.contains("/change-password") || redirect.contains("/confirm-account")) {
			redirect = "/";
		}
		
		response.sendRedirect(redirect);
	}
	
}
