package agroscience.fields;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import agroscience.fields.dao.entities.CropRotation;
import agroscience.fields.dao.entities.Field;
import agroscience.fields.dao.entities.Soil;
import agroscience.fields.dao.repositories.CropRotationRepository;
import agroscience.fields.dao.repositories.CropsRepository;
import agroscience.fields.dao.repositories.FieldRepository;
import agroscience.fields.dao.repositories.SoilRepository;
import agroscience.fields.dto.ResponseMeteo;
import agroscience.fields.dto.field.CoordinatesDTO;
import agroscience.fields.dto.field.GeomDTO;
import agroscience.fields.dto.field.RequestField;
import agroscience.fields.services.FieldService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


public class FieldServiceTest extends AbstractTest {

  @Autowired
  private FieldService fieldService;
  @Autowired
  private FieldRepository fieldRepository;
  @Autowired
  private CropsRepository cropsRepository;
  @Autowired
  private SoilRepository soilRepository;
  @Autowired
  private CropRotationRepository CRRepository;

  @MockBean
  private RestTemplate restTemplate;

  private static Geometry geom() {
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

  private static GeomDTO geomDto() {
    CoordinatesDTO coordinates1 = new CoordinatesDTO(1.0, 2.0);
    CoordinatesDTO coordinates2 = new CoordinatesDTO(3.0, 4.0);
    CoordinatesDTO coordinates3 = new CoordinatesDTO(4.0, 3.0);
    CoordinatesDTO coordinates4 = new CoordinatesDTO(1.0, 2.0);

    List<CoordinatesDTO> coordinatesList = List.of(coordinates1, coordinates2, coordinates3, coordinates4);

    return new GeomDTO("Polygon", coordinatesList);
  }

  @BeforeEach
  @AfterEach
  public void clear() {
    fieldRepository.deleteAll();
    soilRepository.deleteAll();
    CRRepository.deleteAll();
  }

  @Test
  public void createFieldTest() {
    // Given
    var geom = geom();
    var field = Field.builder().fieldColor("FFFFFF").fieldActivityStart(LocalDate.now()).fieldActivityEnd(LocalDate.now())
            .fieldName("field").cropRotations(new ArrayList<>()).soils(new ArrayList<>()).fieldSquareArea("100")
            .fieldOrganizationId(1L).fieldDescription("").fieldGeom(geom).build();
    var field3 = Field.builder().fieldColor("FFFFFF").fieldActivityStart(LocalDate.now()).fieldActivityEnd(LocalDate.now())
            .fieldName("field3").cropRotations(new ArrayList<>()).soils(new ArrayList<>()).fieldSquareArea("100")
            .fieldOrganizationId(1L).fieldDescription("").fieldGeom(geom).build();
    // When
    var testField = fieldService.createField(field);
    System.out.println("hello");
    var testField2 = fieldService.createField(field3);
    // Then
    assertNotNull(testField);
    assertNotNull(testField.getField());
    assertNotNull(testField2);
    assertEquals(testField2.getField().getFieldId(), testField.getField().getFieldId() + 1);
    assertNotNull(testField2.getField());
    assertEquals(2, fieldRepository.findAll().size());
    assertEquals(4, testField.getField().getFieldGeom().getCoordinates().length);
    assertNull(testField.getCropRotation().getCropRotationId());
    assertNotNull(testField.getField().getFieldId());
    assertEquals(testField.getField().getFieldName(), field.getFieldName());
  }

  @Test
  @Transactional
  public void getFullFieldTest() {
    // Given
    var geom = geom();
    var field = Field.builder().fieldColor("FFFFFF").fieldActivityStart(LocalDate.now()).fieldActivityEnd(LocalDate.now())
            .fieldName("field").cropRotations(new ArrayList<>()).soils(new ArrayList<>()).fieldSquareArea("100")
            .fieldOrganizationId(1L).fieldDescription("").fieldGeom(geom).build();
    var crop = cropsRepository.findCropByCropId(1L);
    var CR = CropRotation.builder().crop(crop).
            cropRotationStartDate(LocalDate.now()).cropRotationEndDate(LocalDate.now()).cropRotationDescription("").field(field).build();
    field.getCropRotations().add(CR);
    crop.getCropRotations().add(CR);
    field = fieldRepository.saveAndFlush(field);
    var meteoList = List.of(
            ResponseMeteo.builder().fieldId(field.getFieldId()).day(LocalDate.now()).
                    temperature(1.).pressure(1.).humidity(1.).build(),
            ResponseMeteo.builder().fieldId(field.getFieldId()).day(LocalDate.now()).
                    temperature(1.).pressure(1.).humidity(1.).build()
    );
    ResponseEntity<List<ResponseMeteo>> mockResponse = new ResponseEntity<>(meteoList, HttpStatus.OK);
    when(restTemplate.exchange(
            eq("http://meteo-back:8003/api/v1/meteo/" + field.getFieldId()),
            eq(HttpMethod.GET),
            eq(null),
            any(ParameterizedTypeReference.class)
    )).thenReturn(mockResponse);

    System.out.println("<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>");

    // When
    var result = fieldService.getFullField(field.getFieldId(), field.getFieldOrganizationId());

    // Then
    assertNotNull(result);
    assertNotNull(result);
    assertEquals(field.getFieldId(), result.getFieldId());
    assertEquals(meteoList.get(0).getPressure(), result.getMeteoList().get(0).getPressure());
    assertEquals(meteoList.get(0).getTemperature(), result.getMeteoList().get(0).getTemperature());
    assertEquals(meteoList.get(0).getHumidity(), result.getMeteoList().get(0).getHumidity());
    assertEquals(result.getMeteoList().size(), 2);
  }

  @Test
  @Transactional
  public void deleteFieldTest() {
    // Given
    var geom = geom();
    var field = Field.builder().fieldColor("FFFFFF").fieldActivityStart(LocalDate.now()).fieldActivityEnd(LocalDate.now())
            .fieldName("field").cropRotations(new ArrayList<>()).soils(new ArrayList<>()).fieldSquareArea("100")
            .fieldOrganizationId(1L).fieldDescription("").fieldGeom(geom).build();
    var crop = cropsRepository.findCropByCropId(1L);
    var CR = CropRotation.builder().crop(crop).
            cropRotationStartDate(LocalDate.now()).cropRotationEndDate(LocalDate.now()).cropRotationDescription("").field(field).build();
    field.getCropRotations().add(CR);
    crop.getCropRotations().add(CR);
    var soil = Soil.builder().soilSampleDate(LocalDate.now()).field(field).build();
    field.getSoils().add(soil);
    field = fieldRepository.save(field);

    var fieldId = field.getFieldId();
    var soilId = field.getSoils().get(0).getSoilId();
    var CRId = field.getCropRotations().get(0).getCropRotationId();

    // When
    fieldService.deleteField(field.getFieldId(), field.getFieldOrganizationId());
    fieldRepository.delete(field);
    // Then
    assertFalse(fieldRepository.existsById(fieldId));
    assertFalse(CRRepository.existsById(CRId));
    assertFalse(soilRepository.existsById(soilId));
    assertTrue(cropsRepository.existsById(field.getCropRotations().get(0).getCrop().getCropId()));
  }

  @Test
  @Transactional
  public void updateFieldTest() {
    // Given
    var geom = geom();
    var field = Field.builder().fieldColor("FFFFFF").fieldActivityStart(LocalDate.now()).fieldActivityEnd(LocalDate.now())
            .fieldName("field").cropRotations(new ArrayList<>()).soils(new ArrayList<>()).fieldSquareArea("100")
            .fieldOrganizationId(1L).fieldDescription("").fieldGeom(geom).build();
    var crop = cropsRepository.findCropByCropId(1L);
    var CR = CropRotation.builder().crop(crop).
            cropRotationStartDate(LocalDate.now()).cropRotationEndDate(LocalDate.now()).cropRotationDescription("").field(field).build();
    field.getCropRotations().add(CR);
    crop.getCropRotations().add(CR);
    var soil = Soil.builder().soilSampleDate(LocalDate.now()).field(field).build();
    field.getSoils().add(soil);
    field = fieldRepository.save(field);
    var newFieldRequest = RequestField.builder().fieldActivityStart("10-10-2000").fieldActivityEnd("10-10-2001")
            .fieldGeom(geomDto()).fieldColor("AAAAAA").fieldName("field").fieldDescription("Hi").fieldSquareArea("10").build();

    // When
    var newField = fieldService.updateField(field.getFieldId(), newFieldRequest, field.getFieldOrganizationId());

    // Then
    assertEquals(fieldRepository.findById(newField.getField().getFieldId()).orElseThrow().getSoils().size(), 1);
    assertEquals(fieldRepository.findById(newField.getField().getFieldId()).orElseThrow().getCropRotations().size(), 1);
    assertEquals(fieldRepository.findById(newField.getField().getFieldId()).orElseThrow().getFieldColor(), newFieldRequest.getFieldColor());
    assertEquals(fieldRepository.findAll().size(), 1);
    assertEquals(field.getFieldId(), newField.getField().getFieldId());
  }

}
