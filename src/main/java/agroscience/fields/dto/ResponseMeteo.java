package agroscience.fields.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ResponseMeteo {

  private LocalDate day;
  private Long fieldId;
  private Double temperature;
  private Double humidity;
  private Double pressure;

}
