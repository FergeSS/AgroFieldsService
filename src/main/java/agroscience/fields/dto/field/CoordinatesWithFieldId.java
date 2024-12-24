package agroscience.fields.dto.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoordinatesWithFieldId extends CoordinatesDTO {

  @JsonProperty("id")
  private final Long fieldId;

  public CoordinatesWithFieldId(Long fieldId, Double longitude, Double latitude) {
    super(longitude, latitude);
    this.fieldId = fieldId;
  }

}
