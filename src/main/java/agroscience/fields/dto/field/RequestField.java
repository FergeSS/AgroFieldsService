package agroscience.fields.dto.field;

import agroscience.fields.dto.TimeDTO;
import agroscience.fields.exceptions.validation.constraints.LocalDateFormat;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@LocalDateFormat
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RequestField implements TimeDTO {

  @Size(max = 100, message = "Maximum 100 characters")
  @NotBlank(message = "Must be filled")
  @JsonAlias("name")
  private String fieldName;

  @NotBlank(message = "Must be filled")
  @Size(max = 40, message = "Maximum 40 characters")
  @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "The field must contain only numbers")
  @JsonAlias("squareArea")
  private String fieldSquareArea;

  @Valid
  @JsonAlias("geom")
  private GeomDTO fieldGeom;

  @Size(max = 100, message = "Maximum 256 characters")
  @JsonAlias("description")
  private String fieldDescription;

  @NotBlank(message = "Must be filled")
  @Pattern(regexp = "^([A-Fa-f0-9]{6})$", message = "The color must be written in HEX format")
  @JsonAlias("color")
  private String fieldColor;

  @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$",
          message = "Date entered incorrectly. Must be dd-mm-yyyy.")
  @JsonAlias("activityStart")
  private String fieldActivityStart;
  @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$",
          message = "Date entered incorrectly. Must be dd-mm-yyyy.")
  @JsonAlias("activityEnd")
  private String fieldActivityEnd;

  @Override
  public String start() {
    return fieldActivityStart;
  }

  @Override
  public String end() {
    return fieldActivityEnd;
  }

}
