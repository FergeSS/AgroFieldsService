package agroscience.fields.dto.field;

import agroscience.fields.dto.crop.ResponseCrop;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResponseFieldPreview {

  @JsonProperty("id")
  private Long fieldId;
  @JsonProperty("organizationId")
  private Long fieldOrganizationId;
  @JsonProperty("name")
  private String fieldName;
  @JsonProperty("squareArea")
  private String fieldSquareArea;
  @JsonProperty("geom")
  private GeomDTO fieldGeom;
  @JsonProperty("description")
  private String fieldDescription;
  @Pattern(regexp = "^([A-Fa-f0-9]{6})$", message = "The color must be written in HEX format")
  @JsonProperty("color")
  private String fieldColor;
  @JsonProperty("activityStart")
  private String fieldActivityStart;
  @JsonProperty("activityEnd")
  private String fieldActivityEnd;
  private ResponseCrop crop;

}
