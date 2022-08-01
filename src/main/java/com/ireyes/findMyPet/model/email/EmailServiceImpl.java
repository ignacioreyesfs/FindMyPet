package com.ireyes.findMyPet.model.email;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService{
	@Autowired
	private JavaMailSender emailSender;
	
	@Override
	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}

	@Override
	public void sendEmailWithAttachment(String to, String subject, String text, List<String> filepaths) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		for(String filepath: filepaths) {
			FileSystemResource file = new FileSystemResource(filepath);
			String filename = filepath.replaceAll("(.*(\\/|\\\\))*", "");
			helper.addAttachment(filename, file);
		}
		emailSender.send(message);
	}

}
