package com.ireyes.findMyPet.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeRequests()
				.anyRequest().permitAll()
				.and()
				.formLogin()
					.loginPage("/login")
					.loginProcessingUrl("/login")
					.permitAll();
		
		return http.build();
	}
	
	@Bean
	public UserDetailsManager users(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}
	

}
