package agroscience.fields;

import agroscience.fields.dto.field.CoordinatesDTO;
import agroscience.fields.dto.field.GeomDTO;
import agroscience.fields.v2.entities.Contour;
import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.entities.Season;
import agroscience.fields.v2.entities.SoilComposition;
import agroscience.fields.v2.entities.CropRotationV2;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SampleObjectGenerator {

  public static Geometry geom() {
    CoordinatesDTO coordinates1 = new CoordinatesDTO(1.0, 2.0);
    CoordinatesDTO coordinates2 = new CoordinatesDTO(3.0, 4.0);
    CoordinatesDTO coordinates3 = new CoordinatesDTO(4.0, 3.0);
    CoordinatesDTO coordinates4 = new CoordinatesDTO(1.0, 2.0);

    List<CoordinatesDTO> coordinatesList = List.of(coordinates1, coordinates2, coordinates3, coordinates4);

    var geomDto = new GeomDTO("Polygon", coordinatesList);

    var coordinates = geomDto.getCoordinates();
    GeometryFactory geometryFactory = new GeometryFactory();
    // Преобразовать координаты в массив точек
    Coordinate[] polygonCoordinates = new Coordinate[coordinates.size()];
    for (int i = 0; i < coordinates.size(); i++) {
      polygonCoordinates[i] =
              new Coordinate(coordinates.get(i).getLongitude(), coordinates.get(i).getLatitude());
    }
    return geometryFactory.createPolygon(polygonCoordinates);
  }

  public static Season createSampleSeason() {
    Season season = new Season();
    season.setName("potato");
    season.setEndDate(LocalDate.now());
    season.setArchived(false);
    season.setStartDate(LocalDate.now());
    season.setOrganizationId(UUID.fromString("c536d281-5390-4c5b-b1a3-6206139fce1e"));
    return season;
  }

  public static FieldV2 createSampleFieldAndContourInside(Season season){
    FieldV2 field = new FieldV2();
    field.setSeason(season);
    field.setName("Test Field");
    field.setDescription("Description of the test field");
    Contour contour = new Contour();
    contour.setName("Test Contour");
    contour.setSquareArea("1000");
    contour.setColor("FF5733");
    contour.setGeom(geom());
    contour.setField(field);
    contour.setCropRotations(new ArrayList<>());
    contour.setSoilCompositions(new ArrayList<>());
    field.setContours(List.of(contour));
    return field;
  }

  public static SoilComposition createSampleSoilComposition(Contour contour) {
    SoilComposition soilComposition = new SoilComposition();
    soilComposition.setContour(contour);
    soilComposition.setPh("6.5");
    soilComposition.setSampleDate(LocalDate.now());
    soilComposition.setOrganicMatter("5.2%");
    soilComposition.setMobileP("12.5 mg/kg");
    soilComposition.setMobileK("80 mg/kg");
    soilComposition.setMobileS("15.3 mg/kg");
    soilComposition.setNitrateN("5.1 mg/kg");
    soilComposition.setAmmoniumN("2.3 mg/kg");
    soilComposition.setHydrolyticAcidity("3.5 cmol/kg");
    soilComposition.setCaExchange("15.2 cmol/kg");
    soilComposition.setMgExchange("7.8 cmol/kg");
    soilComposition.setB("0.8 mg/kg");
    soilComposition.setCo("0.05 mg/kg");
    soilComposition.setMn("6.2 mg/kg");
    soilComposition.setZn("1.1 mg/kg");
    Coordinate coordinate = new Coordinate(2,2.5);
    GeometryFactory geometryFactory = new GeometryFactory();
    Point coordinates = geometryFactory.createPoint(coordinate);
    soilComposition.setCoordinates(coordinates);
    return soilComposition;
  }

  public static CropRotationV2 createSampleCropRotation(Contour contour) {
    CropRotationV2 cropRotation = new CropRotationV2();
    cropRotation.setContour(contour);
    cropRotation.setStartDate(LocalDate.now());
    cropRotation.setEndDate(LocalDate.now().plusMonths(6));
    cropRotation.setCulture("Wheat");
    cropRotation.setCultivar("Spring Wheat");
    cropRotation.setDescription("Sample crop rotation description");
    return cropRotation;
  }

  public static Contour createSampleContour(FieldV2 field) {
    Contour contour = new Contour();
    contour.setField(field);
    contour.setName("Sample Contour");
    contour.setSquareArea("1000");
    contour.setGeom(geom());
    contour.setColor("FF5733");
    return contour;
  }

}
