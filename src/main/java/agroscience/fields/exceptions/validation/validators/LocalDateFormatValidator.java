package agroscience.fields.exceptions.validation.validators;

import agroscience.fields.dto.TimeDTO;
import agroscience.fields.exceptions.DateException;
import agroscience.fields.exceptions.validation.constraints.LocalDateFormat;
import agroscience.fields.utilities.LocalDateConverting;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.springframework.data.util.Pair;

public class LocalDateFormatValidator implements ConstraintValidator<LocalDateFormat, TimeDTO> {

  @Override
  public boolean isValid(TimeDTO request, ConstraintValidatorContext context) {

    LocalDate localStartDate = null;
    LocalDate localEndDate = null;
    var ex = new DateException();

    try {
      localStartDate = LocalDateConverting.stringToLocalDate(request.start());
    } catch (Exception e) {
      ex.getFieldErrors().add(Pair.of("start", "Date entered incorrectly"));
    }

    try {
      localEndDate = LocalDateConverting.stringToLocalDate(request.end());
    } catch (Exception e) {
      ex.getFieldErrors().add(Pair.of("end", "Date entered incorrectly"));
    }

    if (localStartDate != null && localEndDate != null && localEndDate.isBefore(localStartDate)) {
      ex.getFieldErrors().add(Pair.of("start", "End date must be later than creation date"));
      ex.getFieldErrors().add(Pair.of("end", "End date must be later than creation date"));
    }

    if (!ex.getFieldErrors().isEmpty()) {
      throw ex;
    }

    return true;
  }

}
