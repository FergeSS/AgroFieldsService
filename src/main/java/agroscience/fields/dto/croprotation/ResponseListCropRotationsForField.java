package agroscience.fields.dto.croprotation;

import java.util.List;
import lombok.Data;

@Data
public class ResponseListCropRotationsForField {

  List<ResponseCRForF> cropRotations;
  private Long fieldId;

}
