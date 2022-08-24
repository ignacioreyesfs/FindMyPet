package com.ireyes.findMyPet.model.user.passwordreset;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ireyes.findMyPet.dao.PasswordResetTokenRepo;

@Component
@Scope("prototype")
public class OnPasswordResetTokenExpire implements Runnable{
	private PasswordResetTokenRepo repo;
	private Long id;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	public OnPasswordResetTokenExpire(PasswordResetTokenRepo repo) {
		this.repo = repo;
	}
	
	@Override
	public void run() {
		if(repo.existsById(id)) {
			repo.deleteById(id);
		}
		logger.info(() -> "Expired password reset token with id " + id.longValue());
	}

	public void setId(Long id) {
		this.id = id;
	}

}
