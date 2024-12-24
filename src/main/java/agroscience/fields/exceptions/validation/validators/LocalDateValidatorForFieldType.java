package agroscience.fields.exceptions.validation.validators;

import agroscience.fields.exceptions.validation.constraints.LocalDateFormat;
import agroscience.fields.utilities.LocalDateConverting;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LocalDateValidatorForFieldType implements ConstraintValidator<LocalDateFormat, String> {

  @Override
  public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
    LocalDate localDate = null;

    try {
      localDate = LocalDateConverting.stringToLocalDate(date);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

}
