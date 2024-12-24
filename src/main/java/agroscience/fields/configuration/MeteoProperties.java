package agroscience.fields.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "meteo")
public class MeteoProperties {

  private String host;
  private String port;

}
