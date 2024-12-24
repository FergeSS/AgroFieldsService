package agroscience.fields.v2.mappers;

import agroscience.fields.v2.entities.FieldV2;
import generated.agroscience.fields.api.model.FieldBaseDTO;
import generated.agroscience.fields.api.model.FieldDTO;
import generated.agroscience.fields.api.model.FieldWithContoursAndCropRotationsDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
            ContourMapper.class,
            ContourMapper.class,
            CropRotationMapperV2.class,
            SoilCompositionMapper.class
        }
)
public interface FieldMapperV2 {

  FieldV2 map(FieldDTO request);

  FieldDTO map(FieldV2 field);

  FieldV2 map(FieldBaseDTO field);

  List<FieldWithContoursAndCropRotationsDTO> map(List<FieldV2> fieldV2List);

}