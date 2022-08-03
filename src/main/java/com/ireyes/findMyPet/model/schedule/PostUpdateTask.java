package com.ireyes.findMyPet.model.schedule;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ireyes.findMyPet.dao.UserRepository;
import com.ireyes.findMyPet.model.email.EmailService;
import com.ireyes.findMyPet.model.user.User;

@Component
public class PostUpdateTask {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	@Qualifier("postUpdateTemplateMessage")
	private SimpleMailMessage template;
	Logger logger = Logger.getLogger(this.getClass().getName());	
	
	@Scheduled(cron = "0 0 7 1 * *")
	public void requestPostUpdates() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		List<User> users = userRepository.findWithPostBefore(calendar.getTime());
		users.forEach(this::sendUpdateRequest);
	}
	
	private void sendUpdateRequest(User user) {
		String email = user.getEmail();
		String name = user.getFirstName() != null? user.getFirstName(): "";
		
		if(email != null) {
			String text = String.format(template.getText(), name);
			try {
				emailService.sendEmail(email, template.getSubject(), text);
			} catch (Exception e) {
				logger.severe("Error sending update post email to " + email);
				e.printStackTrace();
			}
		}
	}
}
