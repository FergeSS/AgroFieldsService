package agroscience.fields.dto.crop;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestCrop {

  @NotBlank
  @Size(max = 50)
  @JsonAlias("name")
  private String cropName;

}
