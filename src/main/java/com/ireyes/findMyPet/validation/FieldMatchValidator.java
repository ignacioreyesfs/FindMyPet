package com.ireyes.findMyPet.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>{
	private String first;
	private String second;
	private String message;
	
	@Override
	public void initialize(FieldMatch annotation) {
		message = annotation.message();
		first = annotation.first();
		second = annotation.second();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object firstValue;
		Object secondValue;
		boolean isValid;
		
		try {
			firstValue = new BeanWrapperImpl(value).getPropertyValue(first);
			secondValue = new BeanWrapperImpl(value).getPropertyValue(second);
		}catch(Exception e) {
			e.printStackTrace();
			buildConstraintViolation(context);
			return false;
		}
		
		isValid = areNullOrEqual(firstValue, secondValue);
		
		if(!isValid){
			buildConstraintViolation(context);
		}
		
		return isValid;
	}
	
	private boolean areNullOrEqual(Object firstValue, Object secondValue) {
		return (firstValue == null && secondValue == null) || (firstValue != null && firstValue.equals(secondValue));
	}
	
	private void buildConstraintViolation(ConstraintValidatorContext context) {
		context.buildConstraintViolationWithTemplate(message)
			.addPropertyNode(first)
			.addConstraintViolation()
			.disableDefaultConstraintViolation();
	}

}
