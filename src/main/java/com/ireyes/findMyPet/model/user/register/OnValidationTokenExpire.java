package com.ireyes.findMyPet.model.user.register;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ireyes.findMyPet.dao.ValidationTokenRepository;

@Component
@Scope("prototype")
public class OnValidationTokenExpire implements Runnable{
	private Long id;
	private ValidationTokenRepository repo;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	public OnValidationTokenExpire(ValidationTokenRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public void run() {
		repo.deleteById(id);
		logger.info(() -> "Expired ValidationToken with id " + id.longValue());
	}

	public void setId(Long id) {
		this.id = id;
	}

}
