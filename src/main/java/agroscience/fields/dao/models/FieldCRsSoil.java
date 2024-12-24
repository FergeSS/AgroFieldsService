package agroscience.fields.dao.models;

import agroscience.fields.dao.entities.Crop;
import agroscience.fields.dao.entities.CropRotation;
import agroscience.fields.dao.entities.Field;
import agroscience.fields.dao.entities.Soil;

public interface FieldCRsSoil {

  Field getField();

  CropRotation getCropRotation();

  Soil getSoil();

  Crop getCrop();

}
