package agroscience.fields.v2.controllers;

import agroscience.fields.v2.security.TokenUserContext;
import org.springframework.security.core.context.SecurityContextHolder;

public interface SecurityController {

  default TokenUserContext token() {
    return (TokenUserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
