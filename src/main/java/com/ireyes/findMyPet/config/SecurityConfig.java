package com.ireyes.findMyPet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ireyes.findMyPet.service.user.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	@Autowired
	private UserService userService;
	@Autowired
	private CustomAuthSuccessHandler authSucessHandler;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/posts/new").authenticated()
				.and()
				.formLogin()
					.loginPage("/login")
					.loginProcessingUrl("/login")
					.successHandler(authSucessHandler)
					.permitAll()
				.and()
				.logout()
					.logoutSuccessUrl("/")
					.permitAll();
		
		return http.build();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}
	
}
