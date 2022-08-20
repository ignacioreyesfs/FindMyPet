package com.ireyes.findMyPet.model.user.token;

import com.ireyes.findMyPet.model.user.User;

public class RegistrationCompleteEvent {
	private User user;
	
	public RegistrationCompleteEvent(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
