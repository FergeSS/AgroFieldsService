package agroscience.fields.dto.crop;

import agroscience.fields.dto.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestGetCrops extends Page {

  @Size(max = 50)
  @JsonProperty("name")
  private String name;

}
