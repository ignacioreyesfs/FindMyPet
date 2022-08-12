package com.ireyes.findMyPet.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

/**
 * Implementation of {@link NotNullIfAnotherFieldHasValue} validator.
 **/
public class NotNullIfAnotherFieldHasValueValidator 
		implements ConstraintValidator<NotNullIfAnotherFieldHasValue, Object>{
	
	private String fieldName;
	private String expectedFieldValue;
	private String dependFieldName;
	private String message;
	
	@Override
	public void initialize(NotNullIfAnotherFieldHasValue annotation) {
		this.fieldName = annotation.fieldName();
		this.expectedFieldValue = annotation.fieldValue();
		this.dependFieldName = annotation.dependFieldName();
		this.message = annotation.message();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		
		String fieldValue = (String) new BeanWrapperImpl(value).getPropertyValue(fieldName);
		Object dependFieldValue = new BeanWrapperImpl(value).getPropertyValue(dependFieldName);
		
		if(fieldValue.equals(expectedFieldValue) && dependFieldValue == null) {
			context.buildConstraintViolationWithTemplate(message)
				.addPropertyNode(dependFieldName)
				.addConstraintViolation()
				.disableDefaultConstraintViolation();
			return false;
		}
		
		return true;
	}
	
}
