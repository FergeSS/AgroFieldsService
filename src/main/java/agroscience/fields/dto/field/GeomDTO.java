package agroscience.fields.dto.field;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeomDTO {

  @NotBlank(message = "Must be filled")
  private String type;
  @Size(min = 3)
  @Valid
  private List<CoordinatesDTO> coordinates;

}
