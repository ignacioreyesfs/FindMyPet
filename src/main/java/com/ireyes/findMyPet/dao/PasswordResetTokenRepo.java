package com.ireyes.findMyPet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ireyes.findMyPet.model.user.User;
import com.ireyes.findMyPet.model.user.passwordreset.PasswordResetToken;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long>{
	public PasswordResetToken findByToken(String token);
	public PasswordResetToken findByUser(User user);
	public void deleteByUser(User user);
}
