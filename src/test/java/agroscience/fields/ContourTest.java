package agroscience.fields;

import agroscience.fields.v2.entities.Contour;
import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.entities.Season;
import agroscience.fields.v2.mappers.ContourMapper;
import agroscience.fields.v2.repositories.ContoursRepository;
import agroscience.fields.v2.repositories.FieldsRepository;
import agroscience.fields.v2.repositories.SeasonsRepository;
import agroscience.fields.v2.services.ContoursService;
import generated.agroscience.fields.api.model.ApiError;
import generated.agroscience.fields.api.model.ContourBaseDTO;
import generated.agroscience.fields.api.model.ExceptionBody;
import generated.agroscience.fields.api.model.IdDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static agroscience.fields.SampleObjectGenerator.createSampleContour;
import static agroscience.fields.SampleObjectGenerator.createSampleFieldAndContourInside;
import static agroscience.fields.SampleObjectGenerator.createSampleSeason;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContourTest extends AbstractTest {

  @Autowired
  private SeasonsRepository seasonRepository;
  @Autowired
  private ContoursRepository contourRepository;
  @Autowired
  private FieldsRepository fieldRepository;
  @Autowired
  private ContoursService contourService;
  @Autowired
  private ContourMapper contourMapper;
  @Autowired
  private HttpSteps httpSteps;

  @BeforeEach
  public void clear() {
    fieldRepository.deleteAll();
    seasonRepository.deleteAll();
  }

  @Test
  public void CreateContourTest(){
    // Given
    Season season = createSampleSeason();
    seasonRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    Contour contour = createSampleContour(field);
    // When
    ContourBaseDTO contourBaseDTO = contourMapper.map(contour);
    String url = "/api/v2/fields-service/fields/" + field.getId().toString() + "/contour";
    ResponseEntity<IdDTO> response = httpSteps.sendPostRequest(contourBaseDTO,url,IdDTO.class);
    // Then
    List<Contour> contourList = contourRepository.findAllByFieldAndArchivedIsFalse(field);
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(IdDTO.class, response.getBody().getClass());
    assertEquals(2,contourList.size());
    contour.setId(contourList.get(1).getId());
    assertEquals(contourList.get(1), contour);
  }

  @Test
  public void getContourTest(){
    // Given
    Season season = createSampleSeason();
    seasonRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    Contour contour = createSampleContour(field);
    contourRepository.save(contour);
    // When
    String url = "/api/v2/fields-service/fields/" + field.getId().toString() + "/contours";
    ResponseEntity<List<ContourBaseDTO>> response = httpSteps.sendGetRequest(url, new ParameterizedTypeReference<>() {});
    //Then
    assertEquals(200, response.getStatusCode().value());
    List<ContourBaseDTO> responseDto = response.getBody();
    assertNotNull(responseDto);
    assertEquals(2,responseDto.size());
    assertEquals(responseDto.get(0),contourMapper.map(field.getContours().get(0)));
    assertEquals(responseDto.get(1),contourMapper.map(contour));
  }

  @Test
  public void deleteContourTest(){
    // Given
    Season season = createSampleSeason();
    seasonRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    //When
    String url = "/api/v2/fields-service/contour";
    ResponseEntity<Void> response = httpSteps.sendDeleteRequest(field.getContours().get(0).getId(),url, "id");
    //Then
    assertEquals(200, response.getStatusCode().value());
    List<Contour> savedContours = contourRepository.findAll();
    assertTrue(savedContours.get(0).isArchived());
    field.setId(savedContours.get(0).getField().getId());
    assertThrows(EntityNotFoundException.class, () -> contourService.findAllByFieldId(field.getId()));
  }

  @Test
  public void putContourTest(){
    // Given
    Season season = createSampleSeason();
    seasonRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    Contour contour = contourRepository.findAll().get(0);
    //When
    contour.setName("sasedskiy");
    ContourBaseDTO contourBaseDTO = contourMapper.map(contour);
    String url = "/api/v2/fields-service/contour";
    ResponseEntity<Void> response = httpSteps.sendPutRequest(contour.getId(), contourBaseDTO, url, "id");
    //Then
    assertEquals(200, response.getStatusCode().value());
    List<Contour> contourList = contourRepository.findAll();
    assertEquals(contourBaseDTO, contourMapper.map(contourList.get(0)));
    assertEquals(contour, contourList.get(0));
  }

  @Test
  public void createInvalidContourTest() {
    //Given
    Season season = createSampleSeason();
    seasonRepository.save(season);
    FieldV2 field = createSampleFieldAndContourInside(season);
    fieldRepository.save(field);
    Contour contourLongName = createSampleContour(field);
    contourLongName.setName("A".repeat(51));
    //When
    ContourBaseDTO contourBaseDTO = contourMapper.map(contourLongName);
    String url = "/api/v2/fields-service/fields/" + field.getId().toString() + "/contour";
    ResponseEntity<ExceptionBody> response = httpSteps.sendPostRequest(contourBaseDTO, url, ExceptionBody.class);
    //Then
    assertEquals(400, response.getStatusCode().value());
    assertNotNull(response.getBody());
    List<ApiError> apiErrors = response.getBody().getErrors();
    assertEquals(1, apiErrors.size());
    assertEquals("name: size must be between 1 and 50", apiErrors.get(0).getDescription());
  }

}
