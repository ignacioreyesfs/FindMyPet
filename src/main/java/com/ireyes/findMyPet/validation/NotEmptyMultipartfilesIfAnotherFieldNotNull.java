package com.ireyes.findMyPet.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = NotEmptyMultipartfilesIfAnotherFieldNullValidator.class)
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@Documented
public @interface NotEmptyMultipartfilesIfAnotherFieldNotNull {
	String multipartsFieldName();
	String anotherFieldName();
	
	String message() default "";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
