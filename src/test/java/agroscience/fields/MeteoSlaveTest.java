package agroscience.fields;

import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.entities.Season;
import agroscience.fields.v2.repositories.FieldsRepository;
import agroscience.fields.v2.repositories.SeasonsRepository;
import generated.agroscience.fields.api.model.MeteoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static agroscience.fields.SampleObjectGenerator.createSampleFieldAndContourInside;
import static agroscience.fields.SampleObjectGenerator.createSampleSeason;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MeteoSlaveTest extends AbstractTest{

  @Autowired
  private SeasonsRepository seasonsRepository;
  @Autowired
  private FieldsRepository fieldRepository;
  @Autowired
  private HttpSteps httpSteps;

  @BeforeEach
  public void clear() {
    seasonsRepository.deleteAll();
    fieldRepository.deleteAll();
  }

  @Test
  public void getCoordinatesTest(){
    // Given
    Season season = createSampleSeason();
    seasonsRepository.save(season);
    FieldV2 field1 = createSampleFieldAndContourInside(season);
    FieldV2 field2 = createSampleFieldAndContourInside(season);
    field2.archive();
    fieldRepository.save(field1);
    fieldRepository.save(field2);
    // When
    String url = "/api/internal/fields-service/fields/all-coordinates";
    ResponseEntity<List<MeteoResponse>> response = httpSteps.sendGetRequest(url, new ParameterizedTypeReference<>() {});
    // Then
    Coordinate coordinate = field1.getContours().get(0).getGeom().getCoordinates()[0];
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(1,response.getBody().size());
    assertEquals(coordinate.getX(),response.getBody().get(0).getLongitude());
    assertEquals(coordinate.getY(),response.getBody().get(0).getLatitude());
    assertEquals(field1.getId(),response.getBody().get(0).getId());
  }

}
