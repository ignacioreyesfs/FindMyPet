package com.ireyes.findMyPet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ireyes.findMyPet.model.user.token.ValidationToken;

public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Long>{
	public ValidationToken findByToken(String token);
}
