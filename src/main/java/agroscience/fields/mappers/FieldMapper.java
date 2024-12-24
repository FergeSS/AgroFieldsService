package agroscience.fields.mappers;

import agroscience.fields.dao.entities.Field;
import agroscience.fields.dao.models.FieldAndCurrentCrop;
import agroscience.fields.dao.models.FieldCRsSoil;
import agroscience.fields.dto.ResponseMeteo;
import agroscience.fields.dto.field.CoordinatesDTO;
import agroscience.fields.dto.field.GeomDTO;
import agroscience.fields.dto.field.RequestField;
import agroscience.fields.dto.field.ResponseFieldPreview;
import agroscience.fields.dto.field.ResponseFieldWithCR;
import agroscience.fields.dto.field.ResponseFullField;
import agroscience.fields.exceptions.WrongCoordinatesException;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
        uses = {FieldMapper.class, CropRotationMapper.class, CropMapper.class, SoilMapper.class})
public interface FieldMapper {

  @Mapping(target = "fieldActivityStart", source = "request.fieldActivityStart", qualifiedByName = "stringToLocalDate")
  @Mapping(target = "fieldActivityEnd", source = "request.fieldActivityEnd", qualifiedByName = "stringToLocalDate")
  @Mapping(target = "fieldGeom", source = "request.fieldGeom", qualifiedByName = "geom")
  Field map(RequestField request, Long fieldOrganizationId);

  @Mapping(target = "fieldActivityStart",
          source = "dto.field.fieldActivityStart", qualifiedByName = "localDateToString")
  @Mapping(target = "fieldActivityEnd", source = "dto.field.fieldActivityEnd", qualifiedByName = "localDateToString")
  @Mapping(target = "fieldGeom", source = "dto.field.fieldGeom", qualifiedByName = "geomToResponseGeom")
  @Mapping(target = "fieldId", source = "dto.field.fieldId")
  @Mapping(target = "fieldOrganizationId", source = "dto.field.fieldOrganizationId")
  @Mapping(target = "fieldName", source = "dto.field.fieldName")
  @Mapping(target = "fieldSquareArea", source = "dto.field.fieldSquareArea")
  @Mapping(target = "fieldDescription", source = "dto.field.fieldDescription")
  @Mapping(target = "fieldColor", source = "dto.field.fieldColor")
  @Mapping(target = "cropRotation", source = "dto.cropRotation")
  @Mapping(target = "soil", source = "dto.soil")
  @Mapping(target = "meteoList", source = "meteoList")
  ResponseFullField map(FieldCRsSoil dto, List<ResponseMeteo> meteoList);

  @Mapping(target = "fieldActivityStart", source = "request.fieldActivityStart", qualifiedByName = "stringToLocalDate")
  @Mapping(target = "fieldActivityEnd", source = "request.fieldActivityEnd", qualifiedByName = "stringToLocalDate")
  @Mapping(target = "fieldGeom", source = "request.fieldGeom", qualifiedByName = "geom")
  void mapToFieldReview(@MappingTarget Field field, RequestField request, Long fieldOrganizationId);

  @Mapping(target = "fieldActivityStart",
          source = "dto.field.fieldActivityStart", qualifiedByName = "localDateToString")
  @Mapping(target = "fieldActivityEnd", source = "field.fieldActivityEnd", qualifiedByName = "localDateToString")
  @Mapping(target = "fieldGeom", source = "dto.field.fieldGeom", qualifiedByName = "geomToResponseGeom")
  @Mapping(target = "fieldId", source = "dto.field.fieldId")
  @Mapping(target = "fieldOrganizationId", source = "dto.field.fieldOrganizationId")
  @Mapping(target = "fieldName", source = "dto.field.fieldName")
  @Mapping(target = "fieldSquareArea", source = "dto.field.fieldSquareArea")
  @Mapping(target = "fieldDescription", source = "dto.field.fieldDescription")
  @Mapping(target = "fieldColor", source = "dto.field.fieldColor")
  @Mapping(target = "crop", source = "dto.cropRotation.crop")
  ResponseFieldPreview mapToFieldReview(FieldAndCurrentCrop dto);

  @Mapping(target = "fieldActivityStart",
          source = "dto.field.fieldActivityStart", qualifiedByName = "localDateToString")
  @Mapping(target = "fieldActivityEnd", source = "dto.field.fieldActivityEnd", qualifiedByName = "localDateToString")
  @Mapping(target = "fieldGeom", source = "dto.field.fieldGeom", qualifiedByName = "geomToResponseGeom")
  @Mapping(target = "fieldId", source = "dto.field.fieldId")
  @Mapping(target = "fieldOrganizationId", source = "dto.field.fieldOrganizationId")
  @Mapping(target = "fieldName", source = "dto.field.fieldName")
  @Mapping(target = "fieldSquareArea", source = "dto.field.fieldSquareArea")
  @Mapping(target = "fieldDescription", source = "dto.field.fieldDescription")
  @Mapping(target = "fieldColor", source = "dto.field.fieldColor")
  @Mapping(target = "cropRotation", source = "dto.cropRotation")
  ResponseFieldWithCR mapToFieldWithCR(FieldAndCurrentCrop dto);

  @Named("geomToResponseGeom")
  default GeomDTO geomToResponseGeom(Geometry geom) {
    if (geom != null) {
      Coordinate[] coordinates = geom.getCoordinates();
      List<CoordinatesDTO> responseCoordinatesList = new ArrayList<>();
      for (Coordinate coordinate : coordinates) {
        responseCoordinatesList.add(new CoordinatesDTO(coordinate.x, coordinate.y));
      }
      return new GeomDTO("Polygon", responseCoordinatesList);
    }
    return null;
  }

  @Named("geom")
  default Geometry requestGeomToGeom(GeomDTO request) {
    var coordinates = request.getCoordinates();
    GeometryFactory geometryFactory = new GeometryFactory();
    // Преобразовать координаты в массив точек
    Coordinate[] polygonCoordinates = new Coordinate[coordinates.size()];
    for (int i = 0; i < coordinates.size(); i++) {
      polygonCoordinates[i] =
              new Coordinate(coordinates.get(i).getLongitude(), coordinates.get(i).getLatitude());
    }
    try {
      var geometry = geometryFactory.createPolygon(polygonCoordinates);
      return geometry;
    } catch (IllegalArgumentException e) {
      throw new WrongCoordinatesException("Points of polygon do not form a closed linestring");
    }
  }

}
