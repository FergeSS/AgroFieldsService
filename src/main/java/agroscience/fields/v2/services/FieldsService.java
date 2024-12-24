package agroscience.fields.v2.services;

import agroscience.fields.v2.entities.Contour;
import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.entities.Season;
import agroscience.fields.v2.repositories.FieldsRepository;
import agroscience.fields.v2.repositories.SeasonsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FieldsService extends DefaultService {

  private final FieldsRepository fieldsRepository;
  private final SeasonsRepository seasonsRepository;

  public FieldV2 save(UUID seasonId, FieldV2 field) {
    Season season = getOrThrow(seasonId, seasonsRepository::findById);
    checkArchived(seasonId, season);
    field.setSeason(season);
    field.getContours().forEach(c -> c.setField(field));
    return fieldsRepository.save(field);
  }

  public void update(UUID fieldId, FieldV2 updateField) {
    FieldV2 field = getOrThrow(fieldId, fieldsRepository::findById);
    checkArchived(fieldId, field);
    field.setName(updateField.getName());
    field.setDescription(updateField.getDescription());
    fieldsRepository.save(field);
  }

  public List<FieldV2> findAll(UUID seasonId) {
    Season season = getOrThrow(seasonId, seasonsRepository::findById);
    checkArchived(seasonId, season);
    return fieldsRepository.findAllBySeasonIdAndArchivedIsFalse(seasonId);
  }

  public void archive(UUID contourId) {
    FieldV2 fieldV2 = getOrThrow(contourId, fieldsRepository::findById);
    fieldV2.archive();
    fieldsRepository.save(fieldV2);
  }
}
