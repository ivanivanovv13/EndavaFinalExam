package com.endava.school.supermarketapi.anotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValidatorConstraint {
    Class<? extends Enum<?>> enumClass();

    String message() default " Invalid enum value!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
