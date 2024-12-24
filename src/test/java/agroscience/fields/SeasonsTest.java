package agroscience.fields;

import agroscience.fields.v2.entities.CropRotationV2;
import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.entities.Season;
import agroscience.fields.v2.mappers.SeasonMapper;
import agroscience.fields.v2.repositories.CropRotationRepositoryV2;
import agroscience.fields.v2.repositories.FieldsRepository;
import agroscience.fields.v2.repositories.SeasonsRepository;
import generated.agroscience.fields.api.model.ApiError;
import generated.agroscience.fields.api.model.ExceptionBody;
import generated.agroscience.fields.api.model.SeasonBaseDTO;
import generated.agroscience.fields.api.model.IdDTO;
import generated.agroscience.fields.api.model.SeasonWithFieldsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import static agroscience.fields.SampleObjectGenerator.createSampleCropRotation;
import static agroscience.fields.SampleObjectGenerator.createSampleFieldAndContourInside;
import static agroscience.fields.SampleObjectGenerator.createSampleSeason;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeasonsTest extends AbstractTest {

  @Autowired
  private SeasonsRepository seasonsRepository;
  @Autowired
  private SeasonMapper seasonMapper;
  @Autowired
  private FieldsRepository fieldRepository;
  @Autowired
  private CropRotationRepositoryV2 cropRotationRepository;
  @Autowired
  private HttpSteps httpSteps;

  @BeforeEach
  public void clear() {
    cropRotationRepository.deleteAll();
    seasonsRepository.deleteAll();
    fieldRepository.deleteAll();
  }

  @Test
  public void createSeasonTest() {
    //Given
    Season season = createSampleSeason();
    //When
    SeasonBaseDTO seasonBaseDTO = seasonMapper.map(List.of(season)).get(0);
    String url = "/api/v2/fields-service/season";
    ResponseEntity<IdDTO> response = httpSteps.sendPostRequest(seasonBaseDTO, url, IdDTO.class);
    //Then
    List<Season> savedSeasons = seasonsRepository.findAll();
    savedSeasons.get(0).setOrganizationId(season.getOrganizationId());
    season.setId(savedSeasons.get(0).getId());
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(IdDTO.class, response.getBody().getClass());
    assertEquals(new IdDTO(savedSeasons.get(0).getId()), response.getBody());
    assertEquals(1, savedSeasons.size());
    assertEquals(season.getId(), savedSeasons.get(0).getId());
    assertEquals(season, savedSeasons.get(0));
  }

  @Test
  public void createInvalidSeasonTest() {
    //Given
    Season SeasonWithLongName = createSampleSeason();
    Season SeasonWithNullDate = createSampleSeason();
    SeasonWithLongName.setName("A".repeat(51));
    SeasonWithNullDate.setStartDate(null);
    //When
    SeasonBaseDTO seasonWithLongNameDTO = seasonMapper.map(List.of(SeasonWithLongName)).get(0);
    SeasonBaseDTO SeasonWithNullDateDTO = seasonMapper.map(List.of(SeasonWithNullDate)).get(0);
    String url = "/api/v2/fields-service/season";
    ResponseEntity<ExceptionBody> responseLongName = httpSteps.sendPostRequest(seasonWithLongNameDTO, url, ExceptionBody.class);
    ResponseEntity<ExceptionBody> responseNullDate = httpSteps.sendPostRequest(SeasonWithNullDateDTO, url, ExceptionBody.class);
    //Then
    assertEquals(400, responseLongName.getStatusCode().value());
    assertEquals(400, responseNullDate.getStatusCode().value());
    assertNotNull(responseLongName.getBody());
    assertNotNull(responseNullDate.getBody());
    List<ApiError> apiErrorsLongName = responseLongName.getBody().getErrors();
    List<ApiError> apiErrorsNullDate = responseNullDate.getBody().getErrors();
    assertEquals(1, apiErrorsLongName.size());
    assertEquals(1, apiErrorsNullDate.size());
    assertEquals("name: size must be between 1 and 50", apiErrorsLongName.get(0).getDescription());
    assertEquals("startDate: must not be null", apiErrorsNullDate.get(0).getDescription());
  }

  @Test
  public void getSeasonTest() {
    //Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    Season season1 = createSampleSeason();
    season1.setStartDate(LocalDate.of(2024, 10, 2));
    seasonsRepository.save(season1);
    FieldV2 field = createSampleFieldAndContourInside(season1);
    fieldRepository.save(field);
    season.setFields(List.of(field));
    season1.setFields(List.of(field));
    CropRotationV2 cropRotation = createSampleCropRotation(field.getContours().get(0));
    cropRotationRepository.save(cropRotation);
    CropRotationV2 cropRotation2 = createSampleCropRotation(field.getContours().get(0));
    cropRotation2.setStartDate(LocalDate.of(2024, 10, 2));
    cropRotationRepository.save(cropRotation2);
    field.getContours().get(0).setCropRotations(List.of(cropRotation2, cropRotation));
    //When
    String url1 = "/api/v2/fields-service/seasons";
    String url2 = "/api/v2/fields-service/seasons/full";
    ResponseEntity<List<SeasonBaseDTO>> response1 = httpSteps.sendGetRequest(url1, new ParameterizedTypeReference<>() {});
    ResponseEntity<List<SeasonWithFieldsDTO>> response2 = httpSteps.sendGetRequest(url2, new ParameterizedTypeReference<>() {});
    //Then
    assertEquals(200, response1.getStatusCode().value());
    assertEquals(200, response2.getStatusCode().value());
    assertNotNull(response1.getBody());
    assertNotNull(response2.getBody());
    assertEquals(2, response1.getBody().size());
    assertEquals(2, response2.getBody().size());
    assertEquals(seasonMapper.map(List.of(season1)).get(0), response1.getBody().get(0));
    assertEquals(seasonMapper.mapWithField(List.of(season1)).get(0), response2.getBody().get(0));
  }

  @Test
  public void deleteSeasonTest() {
    //Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    //When
    String url = "/api/v2/fields-service/season";
    ResponseEntity<Void> response = httpSteps.sendDeleteRequest(season.getId(), url, "id");
    //Then
    assertEquals(200, response.getStatusCode().value());
    assertTrue(seasonsRepository.findAll().get(0).isArchived());
    Sort sort = Sort.by(Sort.Direction.DESC, "startDate");
    assertEquals(0, seasonsRepository.getAllByOrganizationIdAndArchivedIsFalse(season.getOrganizationId(), sort).size());
  }

  @Test
  public void putSeasonTest() {
    //Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    //When
    season.setName("Test");
    SeasonBaseDTO seasonBaseDTO = seasonMapper.map(List.of(season)).get(0);
    String url = "/api/v2/fields-service/season";
    ResponseEntity<Void> response = httpSteps.sendPutRequest(season.getId(), seasonBaseDTO, url, "id");
    //Then
    assertEquals(200, response.getStatusCode().value());
    assertEquals(seasonBaseDTO, seasonMapper.map(seasonsRepository.findAll()).get(0));
    assertEquals(season, seasonsRepository.findAll().get(0));
  }

}
