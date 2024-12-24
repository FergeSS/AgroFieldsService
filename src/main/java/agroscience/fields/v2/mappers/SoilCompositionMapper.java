package agroscience.fields.v2.mappers;

import agroscience.fields.v2.entities.SoilComposition;
import generated.agroscience.fields.api.model.CoordinatesDTO;
import generated.agroscience.fields.api.model.SoilCompositionDTO;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring", uses = {SoilCompositionMapper.class})
public interface SoilCompositionMapper {
  GeometryFactory geometryFactory = new GeometryFactory();

  @Mapping(target = "coordinates", source = "request.coordinates", qualifiedByName = "toGeometry")
  SoilComposition map(SoilCompositionDTO request);

  @Mapping(target = "coordinates", source = "entity.coordinates", qualifiedByName = "toPointDTO")
  SoilCompositionDTO map(SoilComposition entity);

  List<SoilCompositionDTO> map(List<SoilComposition> soilCompositionList);

  @Named("toGeometry")
  default Geometry mapPoint(CoordinatesDTO coordinates) {
    if (coordinates == null) {
      return null;
    }
    return geometryFactory.createPoint(new Coordinate(coordinates.getLongitude(), coordinates.getLatitude()));
  }

  @Named("toPointDTO")
  default CoordinatesDTO mapGeometry(Geometry geometry) {
    if (geometry == null) {
      return null;
    }
    Point coordinates = (Point) geometry;
    return new CoordinatesDTO()
            .latitude(coordinates.getY())
            .longitude(coordinates.getX());
  }

}
