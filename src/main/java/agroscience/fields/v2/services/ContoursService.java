package agroscience.fields.v2.services;

import agroscience.fields.v2.entities.Contour;
import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.repositories.ContoursRepository;
import agroscience.fields.v2.repositories.FieldsRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContoursService extends DefaultService {

  private final ContoursRepository contoursRepository;
  private final FieldsRepository fieldsRepository;

  public Contour findById(UUID contourId) {
    return getOrThrow(contourId, contoursRepository::findById);
  }

  public UUID save(UUID fieldId, Contour contour) {
    FieldV2 field = getOrThrow(fieldId, fieldsRepository::findById);
    checkArchived(fieldId, field);
    contour.setField(field);
    return contoursRepository.save(contour).getId();
  }

  public void archive(UUID contourId) {
    Contour contour = getOrThrow(contourId, contoursRepository::findById);
    contour.archive();
    contoursRepository.save(contour);
  }

  public void update(UUID contourId, Contour updatedContour) {
    Contour contour = getOrThrow(contourId, contoursRepository::findById);
    checkArchived(contourId, contour);
    contour.setName(updatedContour.getName());
    contour.setColor(updatedContour.getColor());
    contoursRepository.save(contour);
  }

  public List<Contour> findAllByFieldId(UUID fieldId) {
    FieldV2 field = getOrThrow(fieldId, fieldsRepository::findById);
    checkArchived(fieldId, field);
    return contoursRepository.findAllByFieldAndArchivedIsFalse(field);
  }

}