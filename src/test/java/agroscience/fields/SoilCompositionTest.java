package agroscience.fields;

import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.entities.Season;
import agroscience.fields.v2.entities.SoilComposition;
import agroscience.fields.v2.mappers.SoilCompositionMapper;
import agroscience.fields.v2.repositories.FieldsRepository;
import agroscience.fields.v2.repositories.SeasonsRepository;
import agroscience.fields.v2.repositories.SoilCompositionsRepository;
import generated.agroscience.fields.api.model.ApiError;
import generated.agroscience.fields.api.model.ExceptionBody;
import generated.agroscience.fields.api.model.IdDTO;
import generated.agroscience.fields.api.model.SoilCompositionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import static agroscience.fields.SampleObjectGenerator.createSampleFieldAndContourInside;
import static agroscience.fields.SampleObjectGenerator.createSampleSeason;
import static agroscience.fields.SampleObjectGenerator.createSampleSoilComposition;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SoilCompositionTest extends AbstractTest {

  @Autowired
  private SeasonsRepository seasonsRepository;
  @Autowired
  private FieldsRepository fieldRepository;
  @Autowired
  private SoilCompositionsRepository soilCompositionsRepository;
  @Autowired
  private SoilCompositionMapper soilCompositionMapper;
  @Autowired
  private HttpSteps httpSteps;

  @BeforeEach
  public void clear() {
    fieldRepository.deleteAll();
    soilCompositionsRepository.deleteAll();
    seasonsRepository.deleteAll();
  }

  @Test
  public void createSoilCompositionTest() {
    // Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    UUID contourId = field.getContours().get(0).getId();
    SoilComposition soilComposition = createSampleSoilComposition(field.getContours().get(0));
    SoilComposition soilCompositionWithOutPoint = createSampleSoilComposition(field.getContours().get(0));
    soilCompositionWithOutPoint.setCoordinates(null);
    // When
    SoilCompositionDTO soilCompositionDTO = soilCompositionMapper.map(soilComposition);
    SoilCompositionDTO soilCompositionDTOWithOutPoint = soilCompositionMapper.map(soilCompositionWithOutPoint);
    String url = "/api/v2/fields-service/contours/" + contourId + "/soil-composition";
    ResponseEntity<IdDTO> response1 = httpSteps.sendPostRequest(soilCompositionDTO, url, IdDTO.class);
    ResponseEntity<IdDTO> response2 = httpSteps.sendPostRequest(soilCompositionDTOWithOutPoint, url, IdDTO.class);
    // Then
    List<SoilComposition> savedSoilCompositions = soilCompositionsRepository.findAll();
    assertEquals(200, response1.getStatusCode().value());
    assertEquals(200, response2.getStatusCode().value());
    assertNotNull(response1.getBody());
    assertNotNull(response2.getBody());
    assertEquals(new IdDTO(savedSoilCompositions.get(0).getId()), response1.getBody());
    assertEquals(new IdDTO(savedSoilCompositions.get(1).getId()), response2.getBody());
    assertEquals(2, savedSoilCompositions.size());
    soilComposition.setId(savedSoilCompositions.get(0).getId());
    assertEquals(soilComposition, savedSoilCompositions.get(0));
    soilComposition.setId(savedSoilCompositions.get(1).getId());
    soilComposition.setCoordinates(savedSoilCompositions.get(1).getCoordinates());
    assertEquals(soilComposition, savedSoilCompositions.get(1));
  }

  @Test
  public void createInvalidSoilCompositionTest() {
    // Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    UUID contourId = field.getContours().get(0).getId();
    SoilComposition SoilCompositionWithLongCo = createSampleSoilComposition(field.getContours().get(0));
    SoilComposition SoilCompositionWithNullDate = createSampleSoilComposition(field.getContours().get(0));
    SoilCompositionWithLongCo.setCo("A".repeat(51));
    SoilCompositionWithNullDate.setSampleDate(null);
    //When
    SoilCompositionDTO soilCompositionWithLongCoDTO = soilCompositionMapper.map(SoilCompositionWithLongCo);
    SoilCompositionDTO SoilCompositionWithNullDateDTO = soilCompositionMapper.map(SoilCompositionWithNullDate);
    String url = "/api/v2/fields-service/contours/" + contourId + "/soil-composition";
    ResponseEntity<ExceptionBody> responseLongCo = httpSteps.sendPostRequest(soilCompositionWithLongCoDTO, url, ExceptionBody.class);
    ResponseEntity<ExceptionBody> responseNullName = httpSteps.sendPostRequest(SoilCompositionWithNullDateDTO, url, ExceptionBody.class);
    //Then
    assertEquals(400, responseLongCo.getStatusCode().value());
    assertEquals(400, responseNullName.getStatusCode().value());
    assertNotNull(responseLongCo.getBody());
    assertNotNull(responseNullName.getBody());
    List<ApiError> apiErrorsLongCo = responseLongCo.getBody().getErrors();
    List<ApiError> apiErrorsNullName = responseNullName.getBody().getErrors();
    assertEquals(1, apiErrorsLongCo.size());
    assertEquals(1, apiErrorsNullName.size());
    assertEquals("co: size must be between 1 and 50", apiErrorsLongCo.get(0).getDescription());
    assertEquals("sampleDate: must not be null", apiErrorsNullName.get(0).getDescription());
  }

  @Test
  public void getSoilCompositionsTest(){
    // Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    UUID contourId = field.getContours().get(0).getId();
    SoilComposition soilComposition1 = createSampleSoilComposition(field.getContours().get(0));
    SoilComposition soilComposition2 = createSampleSoilComposition(field.getContours().get(0));
    soilComposition2.setSampleDate(LocalDate.of(2024, 10, 2));
    soilCompositionsRepository.save(soilComposition1);
    soilCompositionsRepository.save(soilComposition2);
    // When
    String url = "/api/v2/fields-service/contours/" + contourId + "/soil-compositions";
    ResponseEntity<List<SoilCompositionDTO>> response = httpSteps.sendGetRequest(url, new ParameterizedTypeReference<>() {});
    //Then
    assertEquals(200, response.getStatusCode().value());
    List<SoilCompositionDTO> responseDto = response.getBody();
    assertNotNull(responseDto);
    assertEquals(2,responseDto.size());
    assertEquals(responseDto.get(1),soilCompositionMapper.map(soilComposition1));
    assertEquals(responseDto.get(0),soilCompositionMapper.map(soilComposition2));
  }

  @Test
  public void deleteSoilCompositionTest(){
    // Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    SoilComposition soilComposition = createSampleSoilComposition(field.getContours().get(0));
    soilCompositionsRepository.save(soilComposition);
    //When
    String url = "/api/v2/fields-service/soil-composition";
    ResponseEntity<Void> response = httpSteps.sendDeleteRequest(soilComposition.getId(),url, "id");
    //Then
    assertEquals(200, response.getStatusCode().value());
    List<SoilComposition> savedSoilCompositions = soilCompositionsRepository.findAll();
    assertTrue(savedSoilCompositions.get(0).isArchived());
    Sort sort = Sort.by(Sort.Direction.DESC, "sampleDate");
    List<SoilComposition> savedSoilCompositionsAndNotArchived = soilCompositionsRepository.findAllByContourAndArchivedIsFalse(field.getContours().get(0), sort);
    assertEquals(0,savedSoilCompositionsAndNotArchived.size());
  }

  @Test
  public void putSoilCompositionTest(){
    // Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    SoilComposition soilComposition = createSampleSoilComposition(field.getContours().get(0));
    soilCompositionsRepository.save(soilComposition);
    //When
    soilComposition.setCo("KoKOKOkO");
    SoilCompositionDTO soilCompositionDTO = soilCompositionMapper.map(soilComposition);
    String url = "/api/v2/fields-service/soil-composition";
    ResponseEntity<Void> response = httpSteps.sendPutRequest(soilComposition.getId(), soilCompositionDTO, url, "id");
    //Then
    assertEquals(200, response.getStatusCode().value());
    List<SoilComposition> soilCompositions = soilCompositionsRepository.findAll();
    assertEquals(soilCompositionDTO, soilCompositionMapper.map(soilCompositions.get(0)));
    assertEquals(soilComposition, soilCompositions.get(0));
  }

}
