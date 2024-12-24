package agroscience.fields.dao.models;

import agroscience.fields.dao.entities.Crop;
import agroscience.fields.dao.entities.CropRotation;
import agroscience.fields.dao.entities.Field;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FandCRandCImpl implements FandCRandC {

  private final Field field;
  private final CropRotation cropRotation;
  private final Crop crop;

}
