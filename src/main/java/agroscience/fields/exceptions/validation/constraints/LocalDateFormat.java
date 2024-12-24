package agroscience.fields.exceptions.validation.constraints;

import agroscience.fields.exceptions.validation.validators.LocalDateFormatValidator;
import agroscience.fields.exceptions.validation.validators.LocalDateValidatorForFieldType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {LocalDateFormatValidator.class, LocalDateValidatorForFieldType.class})
public @interface LocalDateFormat {

  String message() default "Дата введена неверно";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
