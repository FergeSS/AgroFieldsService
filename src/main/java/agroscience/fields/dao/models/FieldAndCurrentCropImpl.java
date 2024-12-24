package agroscience.fields.dao.models;

import agroscience.fields.dao.entities.CropRotation;
import agroscience.fields.dao.entities.Field;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldAndCurrentCropImpl implements FieldAndCurrentCrop {

  private final Field field;
  private final CropRotation cropRotation;

}
