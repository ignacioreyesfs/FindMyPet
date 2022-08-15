package com.ireyes.findMyPet.validation;


import java.util.List;
import java.util.logging.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.multipart.MultipartFile;

public class NotEmptyMultipartfilesIfAnotherFieldNullValidator 
	implements ConstraintValidator<NotEmptyMultipartfilesIfAnotherFieldNotNull, Object>{
	
	private String multipartsFieldName;
	private String anotherFieldName;
	private String message;
	
	@Override
	public void initialize(NotEmptyMultipartfilesIfAnotherFieldNotNull annotation) {
		this.multipartsFieldName = annotation.multipartsFieldName();
		this.anotherFieldName = annotation.anotherFieldName();
		this.message = annotation.message();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		
		Object multiparts = new BeanWrapperImpl(value).getPropertyValue(multipartsFieldName);
		Object anotherFieldValue = new BeanWrapperImpl(value).getPropertyValue(anotherFieldName);
		
		if(anotherFieldValue != null) {
			return true;
		}
		
		if(multiparts == null) {
			setConstriantViolation(context);
			return false;
		}
		
		if(!(multiparts instanceof List<?>) || ((List<?>)multiparts).isEmpty() 
				|| !(((List<?>)multiparts).get(0) instanceof MultipartFile)){
			setConstriantViolation(context);
			return false;
		}
		
		List<MultipartFile> castedMultiparts = (List<MultipartFile>) multiparts;
		
		if(castedMultiparts.stream()
				.filter(multipart -> !multipart.isEmpty() && !multipart.getOriginalFilename().equals("")).toList().isEmpty()) {
			setConstriantViolation(context);
			return false;
		}
		
		return true;
	}
	
	private void setConstriantViolation(ConstraintValidatorContext context) {
		context.buildConstraintViolationWithTemplate(message)
		.addPropertyNode(multipartsFieldName)
		.addConstraintViolation()
		.disableDefaultConstraintViolation();
	}


}
