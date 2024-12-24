package agroscience.fields.mappers;

import agroscience.fields.dao.entities.CropRotation;
import agroscience.fields.dao.models.FandCRandC;
import agroscience.fields.dto.croprotation.RequestCropRotation;
import agroscience.fields.dto.croprotation.ResponseCRForF;
import agroscience.fields.dto.croprotation.ResponseCRWithField;
import agroscience.fields.dto.croprotation.ResponseListCropRotationsForField;
import agroscience.fields.utilities.LocalDateConverting;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = CropMapper.class)
public interface CropRotationMapper {

  @Mapping(target = "cropRotationStartDate", source = "cropRotationStartDate", qualifiedByName = "localDateToString")
  @Mapping(target = "cropRotationEndDate", source = "cropRotationEndDate", qualifiedByName = "localDateToString")
  @Mapping(target = "cropName", source = "crop", qualifiedByName = "cropName")
  List<ResponseCRForF> map(List<CropRotation> cropRotations);

  @Mapping(target = "cropRotations", source = "cropRotationResponses")
  ResponseListCropRotationsForField map(Long fieldId, List<ResponseCRForF> cropRotationResponses);

  @Mapping(target = "cropRotationStartDate",
          source = "dto.cropRotation.cropRotationStartDate", qualifiedByName = "localDateToString")
  @Mapping(target = "cropRotationEndDate",
          source = "dto.cropRotation.cropRotationEndDate", qualifiedByName = "localDateToString")
  @Mapping(target = "crop", source = "dto.crop")
  @Mapping(target = "cropRotationId", source = "dto.cropRotation.cropRotationId")
  @Mapping(target = "cropRotationDescription", source = "dto.cropRotation.cropRotationDescription")
  @Mapping(target = "field", source = "dto.field")
  ResponseCRWithField map(FandCRandC dto);

  @Mapping(target = "cropRotationStartDate", source = "cropRotationStartDate", qualifiedByName = "stringToLocalDate")
  @Mapping(target = "cropRotationEndDate", source = "cropRotationEndDate", qualifiedByName = "stringToLocalDate")
  @Mapping(target = "field", ignore = true)
  @Mapping(target = "crop", ignore = true)
  CropRotation map(RequestCropRotation request);

  @Named("localDateToString")
  default String localDateToString(LocalDate date) {
    return LocalDateConverting.localDateToString(date);
  }

  @Named("stringToLocalDate")
  default LocalDate localDateToString(String date) throws ParseException {
    return LocalDateConverting.stringToLocalDate(date);
  }

  @Mapping(target = "cropRotationId", ignore = true)
  @Mapping(target = "field", ignore = true)
  @Mapping(target = "crop", ignore = true)
  void newCRToCR(@MappingTarget CropRotation cropRotation, CropRotation newCropRotation);

}
