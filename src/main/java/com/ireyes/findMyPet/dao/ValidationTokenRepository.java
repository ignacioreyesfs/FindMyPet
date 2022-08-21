package com.ireyes.findMyPet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.model.user.register.ValidationToken;

public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Long>{
	public ValidationToken findByToken(String token);
	public ValidationToken findByUser(User user);
	public void deleteByUser(User user);
}
