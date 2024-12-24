package agroscience.fields.v2.mappers;


import agroscience.fields.v2.entities.CropRotationV2;
import generated.agroscience.fields.api.model.CropRotationDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CropRotationMapperV2.class})
public interface CropRotationMapperV2 {

  CropRotationV2 map(CropRotationDTO request);

  CropRotationDTO map(CropRotationV2 cropRotationV2);

  List<CropRotationDTO> map(List<CropRotationV2> cropRotationV2);

}
