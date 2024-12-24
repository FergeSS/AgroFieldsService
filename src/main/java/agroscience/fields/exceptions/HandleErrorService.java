package agroscience.fields.exceptions;

import static java.lang.String.format;
import static java.util.Objects.requireNonNullElse;

import generated.agroscience.fields.api.model.ApiError;
import generated.agroscience.fields.api.model.ExceptionBody;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class HandleErrorService {

  private static final String BAD_REQUEST_MESSAGE = "Bad request";

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionBody handleValidationException(MethodArgumentNotValidException ex) {
    String message = "%s: %s";
    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    List<ApiError> apiErrors = errors.stream()
            .map(e -> new ApiError(
                    BAD_REQUEST_MESSAGE,
                    format(
                            message,
                            e.getField(),
                            requireNonNullElse(e.getDefaultMessage(), "No message available"))
                    )
            ).toList();
    return new ExceptionBody(apiErrors);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionBody handleEntityNotFoundException(EntityNotFoundException ex) {
    return exceptionBody(BAD_REQUEST_MESSAGE, ex.getMessage());
  }

  @ExceptionHandler(WrongCoordinatesException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionBody handleWrongCoordinatesException(Exception e) {
    log.error("Internal error", e);
    return exceptionBody("Interlan error", "Unknown exception");
  }

  private ExceptionBody exceptionBody(String title, String description) {
    return new ExceptionBody(List.of(new ApiError(title, description)));
  }

}