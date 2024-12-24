package agroscience.fields.v2.security;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends CustomFilter {

  @Override
  protected final void doFilterInternal(
          final @NonNull HttpServletRequest request,
          final @NonNull HttpServletResponse response,
          final @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (isInWhiteList(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    var tokenUserContextOpt = getTokenUserContext(request);
    if (tokenUserContextOpt.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    TokenUserContext userDetails = tokenUserContextOpt.get();
    var user = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(user);

    filterChain.doFilter(request, response);
  }

  private Optional<TokenUserContext> getTokenUserContext(HttpServletRequest request) {
    String jwt;
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      jwt = header.substring(7); // Убираем "Bearer " из заголовка
    } else {
      log.error("Bearer prefix or Authorization header missing");
      return Optional.empty();
    }

    try {
      SignedJWT decodedJWT = SignedJWT.parse(jwt);
      var claims = decodedJWT.getJWTClaimsSet().getClaims();

      return Optional.of(new TokenUserContext(role(claims), orgId(claims)));
    } catch (Exception e) {
      log.error("Get error while parsing jwt", e);
      return Optional.empty();
    }
  }

  private String role(Map<String, Object> claims) {
    return (String) claims.get("role");
  }

  private UUID orgId(Map<String, Object> claims) {
    return UUID.fromString((String) claims.get("org"));
  }

}