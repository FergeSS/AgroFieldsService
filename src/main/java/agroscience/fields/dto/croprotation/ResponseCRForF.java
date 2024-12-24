package agroscience.fields.dto.croprotation;

import agroscience.fields.dto.crop.ResponseCrop;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseCRForF {

  @JsonProperty("id")
  private Long cropRotationId;
  private ResponseCrop crop;
  @JsonProperty("description")
  private String cropRotationDescription;
  @JsonProperty("startDate")
  private String cropRotationStartDate;
  @JsonProperty("endDate")
  private String cropRotationEndDate;

}
