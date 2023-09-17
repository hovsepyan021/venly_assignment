package com.example.demo.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AddWordValidator.class)
@Documented
public @interface ValidAddWord {
    String message() default "Invalid add word request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
