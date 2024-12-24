package agroscience.fields.security;

import agroscience.fields.exceptions.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthoriseService {

  private final JwtFilter filter;

  /**
   * Checks the correctness of jwt, performs authorization, gives the id of the organization.
   */
  public Long doFilter(HttpServletRequest request, List<Role> roles) {
    Role role = null;
    Map<String, Object> claims = null;
    Long orgId = null;
    try {
      claims = filter.extractJwtFromRequest(request);
    } catch (Exception e) {
      throw new AuthException("Invalid jwt.");
    }

    try {
      role = filter.extractRoleFromJwt(claims);
    } catch (Exception e) {
      throw new AuthException("Invalid role");
    }

    if (!roles.contains(role)) {
      throw new AuthException("Access denied, one of the roles is required: " + roles);
    }

    try {
      orgId = filter.extractOrganizationIdFromJwt(claims);
    } catch (Exception e) {
      throw new AuthException("Invalid organization id");
    }
    return orgId;
  }

}
