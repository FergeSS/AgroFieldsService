package agroscience.fields.dto.crop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseCrop {

  @JsonProperty("id")
  private Long cropId;
  @JsonProperty("name")
  private String cropName;

}
