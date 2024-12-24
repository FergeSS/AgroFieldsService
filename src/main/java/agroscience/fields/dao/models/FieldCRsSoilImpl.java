package agroscience.fields.dao.models;


import agroscience.fields.dao.entities.Crop;
import agroscience.fields.dao.entities.CropRotation;
import agroscience.fields.dao.entities.Field;
import agroscience.fields.dao.entities.Soil;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldCRsSoilImpl implements FieldCRsSoil {

  private final Field field;
  private final CropRotation cropRotation;
  private final Soil soil;
  private final Crop crop;

}
