package agroscience.fields;

import agroscience.fields.v2.entities.CropRotationV2;
import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.entities.Season;
import agroscience.fields.v2.mappers.FieldMapperV2;
import agroscience.fields.v2.repositories.CropRotationRepositoryV2;
import agroscience.fields.v2.repositories.FieldsRepository;
import agroscience.fields.v2.repositories.SeasonsRepository;
import generated.agroscience.fields.api.model.ApiError;
import generated.agroscience.fields.api.model.ExceptionBody;
import generated.agroscience.fields.api.model.FieldDTO;
import generated.agroscience.fields.api.model.FieldBaseDTO;
import generated.agroscience.fields.api.model.IdDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static agroscience.fields.SampleObjectGenerator.createSampleCropRotation;
import static agroscience.fields.SampleObjectGenerator.createSampleFieldAndContourInside;
import static agroscience.fields.SampleObjectGenerator.createSampleSeason;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldV2Test extends AbstractTest {

  @Autowired
  private SeasonsRepository seasonsRepository;
  @Autowired
  private FieldsRepository fieldRepository;
  @Autowired
  private FieldTestRepository fieldTestRepository;
  @Autowired
  private CropRotationRepositoryV2 cropRotationRepository;
  @Autowired
  private FieldMapperV2 fieldMapperV2;
  @Autowired
  private HttpSteps httpSteps;

  @BeforeEach
  public void clear() {
    cropRotationRepository.deleteAll();
    fieldRepository.deleteAll();
    seasonsRepository.deleteAll();
  }

  @Test
  public void createFieldTest() {
    //Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    //When
    FieldBaseDTO fieldBaseDTO = fieldMapperV2.map(field);
    String url = "/api/v2/fields-service/seasons/" + season.getId() + "/field";
    ResponseEntity<IdDTO> response = httpSteps.sendPostRequest(fieldBaseDTO, url, IdDTO.class);
    //Then
    List<FieldV2> savedFields = fieldRepository.findAll();
    field.setId(savedFields.get(0).getId());
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(IdDTO.class, response.getBody().getClass());
    assertEquals(new IdDTO(savedFields.get(0).getId()), response.getBody());
    assertEquals(1, savedFields.size());
    assertEquals(field, savedFields.get(0));
  }

  @Test
  public void getFieldTest() {
    //Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    //When
    String url = "/api/v2/fields-service/seasons/" + season.getId() + "/fields";
    ResponseEntity<List<FieldBaseDTO>> response = httpSteps.sendGetRequest(url, new ParameterizedTypeReference<>() {});
    //Then
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals(fieldMapperV2.map(List.of(field)).get(0), response.getBody().get(0));
  }

  @Test
  public void deleteFieldTest() {
    //Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    //When
    String url = "/api/v2/fields-service/field";
    ResponseEntity<Void> response = httpSteps.sendDeleteRequest(field.getId(), url, "id");
    //Then
    assertEquals(200, response.getStatusCode().value());
    assertTrue(fieldRepository.findAll().get(0).isArchived());
    assertEquals(0, fieldRepository.getAllByIdAndArchivedIsFalse(field.getId()).size());
  }

  @Test
  public void putFieldTest() {
    //Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldTestRepository.save(field);
    CropRotationV2 cropRotationV2 = createSampleCropRotation(field.getContours().get(0));
    cropRotationRepository.save(cropRotationV2);
    //When
    field.setName("Test");
    FieldDTO fieldDTO = fieldMapperV2.map(field);
    String url = "/api/v2/fields-service/field";
    ResponseEntity<Void> response = httpSteps.sendPutRequest(field.getId(), fieldDTO, url, "id");
    //Then
    assertEquals(200, response.getStatusCode().value());
    assertEquals(fieldDTO, fieldMapperV2.map(fieldTestRepository.findAllByArchivedIsFalse().get(0)));
    assertEquals(field, fieldTestRepository.findAll().get(0));
  }

  @Test
  public void createInvalidFieldTest() {
    //Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    field.setName("A".repeat(51));
    //When
    FieldBaseDTO fieldBaseDTO = fieldMapperV2.map(field);
    String url = "/api/v2/fields-service/seasons/" + season.getId() + "/field";
    ResponseEntity<ExceptionBody> response = httpSteps.sendPostRequest(fieldBaseDTO, url, ExceptionBody.class);
    //Then
    assertEquals(400, response.getStatusCode().value());
    assertNotNull(response.getBody());
    List<ApiError> apiErrors = response.getBody().getErrors();
    assertEquals(1, apiErrors.size());
    assertEquals("name: size must be between 1 and 50", apiErrors.get(0).getDescription());
  }

}
