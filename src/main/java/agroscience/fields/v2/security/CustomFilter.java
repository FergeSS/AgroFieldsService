package agroscience.fields.v2.security;

import static agroscience.fields.v2.security.Constants.WHITE_LIST_URL;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public abstract class CustomFilter extends OncePerRequestFilter {

  protected static boolean isInWhiteList(HttpServletRequest request) {
    var matcher = new AntPathMatcher();
    var path = request.getServletPath();
    for (String url : WHITE_LIST_URL) {
      if (matcher.match(url, path)) {
        return true;
      }
    }
    return false;
  }

}