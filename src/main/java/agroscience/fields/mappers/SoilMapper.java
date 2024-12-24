package agroscience.fields.mappers;

import agroscience.fields.dao.entities.Field;
import agroscience.fields.dao.entities.Soil;
import agroscience.fields.dto.soil.RequestSoil;
import agroscience.fields.dto.soil.RequestSoilWithouFieldId;
import agroscience.fields.dto.soil.ResponseSoil;
import agroscience.fields.utilities.LocalDateConverting;
import java.text.ParseException;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SoilMapper {

  @Mapping(target = "soilSampleDate", source = "soilSampleDate", qualifiedByName = "stringToLocalDate2")
  Soil map(RequestSoil requestSoil);

  @Mapping(target = "soilSampleDate", source = "soilSampleDate", qualifiedByName = "stringToLocalDate2")
  Soil map(RequestSoilWithouFieldId request);

  @Mapping(target = "soilSampleDate", source = "soilSampleDate", qualifiedByName = "localDateToString2")
  @Mapping(target = "fieldId", source = "field", qualifiedByName = "fieldId")
  ResponseSoil map(Soil soil);

  @Mapping(target = "soilId", ignore = true)
  @Mapping(target = "field", ignore = true)
  void map(@MappingTarget Soil soil, Soil newSoil);

  @Named("localDateToString2")
  default String localDateToString2(LocalDate date) {
    return LocalDateConverting.localDateToString(date);
  }

  @Named("stringToLocalDate2")
  default LocalDate localDateToString2(String date) throws ParseException {
    return LocalDateConverting.stringToLocalDate(date);
  }

  @Named("fieldId")
  default Long getFieldId(Field field) {
    return field.getFieldId();
  }

}