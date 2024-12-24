package agroscience.fields.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthException extends RuntimeException {

  private final String message;

  public AuthException(String message) {
    this.message = message;
  }

}
