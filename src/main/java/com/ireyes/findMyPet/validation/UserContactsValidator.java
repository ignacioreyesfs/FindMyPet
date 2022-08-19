package com.ireyes.findMyPet.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ireyes.findMyPet.model.user.Contact;
import com.ireyes.findMyPet.model.user.ContactType;

public class UserContactsValidator implements ConstraintValidator<ValidUserContacts, List<Contact>>{
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PHONE_PATTERN = "^[\\d]{1,14}$";
	
	@Override
	public boolean isValid(List<Contact> contacts, ConstraintValidatorContext context) {
		String message;
		Pattern emailPattern;
		Pattern numberPattern;
		Matcher matcher;
		
		if(contacts == null) {
			return true;
		}
		
		emailPattern = Pattern.compile(EMAIL_PATTERN);
		numberPattern = Pattern.compile(PHONE_PATTERN);
		
		for(Contact contact: contacts) {
			if(contact.getType().equals(ContactType.EMAIL)) {
				matcher = emailPattern.matcher(contact.getValue());
				message = "Invalid email format: " + contact.getValue();
			}else if(contact.getType().equals(ContactType.MOBILE) || contact.getType().equals(ContactType.PHONE)) {
				matcher = numberPattern.matcher(contact.getValue());
				message = "Invalid number format: " + contact.getValue();
			}else {
				continue;
			}
			
			if(!matcher.matches()) {
				setConstraintViolation(context, message);
				return false;
			}
		}
		
		return true;
	}
	
	private void setConstraintViolation(ConstraintValidatorContext context, String message) {
		context.buildConstraintViolationWithTemplate(message)
			.addConstraintViolation()
			.disableDefaultConstraintViolation();
	}

}
