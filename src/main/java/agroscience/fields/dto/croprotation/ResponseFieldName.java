package agroscience.fields.dto.croprotation;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseFieldName {

  @JsonProperty("id")
  private Long fieldId;
  @JsonAlias("name")
  private String fieldName;

}
