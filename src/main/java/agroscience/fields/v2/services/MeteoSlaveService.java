package agroscience.fields.v2.services;

import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.repositories.FieldsRepository;
import generated.agroscience.fields.api.model.MeteoResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeteoSlaveService extends DefaultService {

  private final FieldsRepository fieldsRepository;

  @Transactional
  public List<MeteoResponse> getAllFieldCoordinates() {
    List<FieldV2> fieldList = fieldsRepository.findAllByArchivedIsFalse();
    List<MeteoResponse> responseList = new ArrayList<>();
    fieldList.forEach(field -> {
      Coordinate coordinate = field.getContours().get(0).getGeom().getCoordinates()[0];
      responseList.add(new MeteoResponse(coordinate.getX(), coordinate.getY(), field.getId()));
    });
    return responseList;
  }

}
