package com.ireyes.findMyPet.model.email;

import java.util.List;

public interface EmailService {
	public void sendEmail(String to, String subject, String text) throws Exception;
	public void sendEmailWithAttachment(String to, String subject, String text, List<String> filepaths) throws Exception;
}
