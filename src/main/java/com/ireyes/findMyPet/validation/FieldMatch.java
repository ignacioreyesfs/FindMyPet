package com.ireyes.findMyPet.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = FieldMatchValidator.class)
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@Documented
public @interface FieldMatch {
	String first();
	String second();
	
	String message() default "Fields not match";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
