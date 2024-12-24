package agroscience.fields.dto.field;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesDTO {

  @NotNull(message = "Must be filled")
  private Double longitude;
  @NotNull(message = "Must be filled")
  private Double latitude;

}
