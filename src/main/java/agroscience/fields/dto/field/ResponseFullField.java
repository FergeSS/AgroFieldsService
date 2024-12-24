package agroscience.fields.dto.field;

import agroscience.fields.dto.ResponseMeteo;
import agroscience.fields.dto.croprotation.ResponseCRForF;
import agroscience.fields.dto.soil.ResponseSoilForF;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Data;

@Data
public class ResponseFullField {

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
  private ResponseCRForF cropRotation;
  private ResponseSoilForF soil;
  private List<ResponseMeteo> meteoList;

}
