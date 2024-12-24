package agroscience.fields.exceptions;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleErrorService {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionBody handleValidationException(MethodArgumentNotValidException ex) {
    ExceptionBody exceptionBody = new ExceptionBody("Validation failed.");
    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    exceptionBody.setErrors(errors.stream()
            .collect(Collectors.toMap(FieldError::getField,
                    FieldError::getDefaultMessage)));
    return exceptionBody;
  }

  @ExceptionHandler(DateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionBody handleValidationException(DateException ex) {
    ExceptionBody exceptionBody = new ExceptionBody("Validation failed.");
    Map<String, String> errorMap = new HashMap<>();
    ex.getFieldErrors().forEach(fieldError -> {
      errorMap.put(fieldError.getFirst(), fieldError.getSecond());
    });
    exceptionBody.setErrors(errorMap);

    return exceptionBody;
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionBody handleEntityNotFoundException(EntityNotFoundException ex) {
    var exceptionBody = new ExceptionBody("Model not found.");
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("message", ex.getMessage());
    exceptionBody.setErrors(errorResponse);
    return exceptionBody;
  }

  @ExceptionHandler(DuplicateException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ExceptionBody handleDuplicateException(DuplicateException ex) {
    var exceptionBody = new ExceptionBody("Duplicate exception.");
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put(ex.getFieldName(), ex.getMessage());
    exceptionBody.setErrors(errorMap);
    return exceptionBody;
  }

  @ExceptionHandler(AuthException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ExceptionBody handleAuthException(AuthException ex) {
    var exceptionBody = new ExceptionBody("Auth exception.");
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("auth", ex.getMessage());
    exceptionBody.setErrors(errorMap);
    return exceptionBody;
  }

  @ExceptionHandler(WrongCoordinatesException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionBody handleWrongCoordinatesException(WrongCoordinatesException ex) {
    var exceptionBody = new ExceptionBody("Validation failed.");
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("geom", ex.getMessage());
    exceptionBody.setErrors(errorMap);
    return exceptionBody;
  }

}
