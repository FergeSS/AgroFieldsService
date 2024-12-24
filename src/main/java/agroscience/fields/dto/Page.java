package agroscience.fields.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Page {

  @Min(value = 0, message = "'pageNumber' must be greater than or equal to 0")
  protected int page = 0;

  @Min(value = 1, message = "'pageSize' must be greater than or equal to 1")
  @Max(value = 5000, message = "'pageSize' must be less than or equal to 5000")
  protected int size = 50;

}