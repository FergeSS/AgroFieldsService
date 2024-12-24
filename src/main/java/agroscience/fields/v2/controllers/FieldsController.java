package agroscience.fields.v2.controllers;

import agroscience.fields.v2.entities.CropRotationV2;
import agroscience.fields.v2.entities.FieldV2;
import agroscience.fields.v2.mappers.FieldMapperV2;
import agroscience.fields.v2.services.FieldsService;
import generated.agroscience.fields.api.FieldsApi;
import generated.agroscience.fields.api.model.FieldBaseDTO;
import generated.agroscience.fields.api.model.FieldDTO;
import generated.agroscience.fields.api.model.FieldWithContoursAndCropRotationsDTO;
import generated.agroscience.fields.api.model.IdDTO;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('organization') or hasRole('worker')")
public class FieldsController implements FieldsApi {

  private final FieldMapperV2 fieldMapperV2;
  private final FieldsService fieldService;

  @Override
  public void changeField(UUID fieldId, FieldBaseDTO fieldBaseDTO) {
    FieldV2 updateField = fieldMapperV2.map(fieldBaseDTO);
    fieldService.update(fieldId, updateField);
  }

  @Override
  public void deleteField(UUID fieldId) {
    fieldService.archive(fieldId);
  }

  @Override
  public List<FieldWithContoursAndCropRotationsDTO> findFields(UUID seasonId) {
    List<FieldV2> fields = fieldService.findAll(seasonId);
    fields.forEach(fieldV2 -> {
      fieldV2.getContours().forEach(contour -> {
        List<CropRotationV2> latestCrop = contour.getCropRotations().stream()
                .sorted(Comparator.comparing(CropRotationV2::getStartDate).reversed())
                .limit(1)
                .toList();
        contour.setCropRotations(latestCrop);
      });
    });
    return fieldMapperV2.map(fields);
  }

  @Override
  public IdDTO saveField(UUID seasonId, FieldDTO fieldDTO) {
    var fieldEntity = fieldMapperV2.map(fieldDTO);
    var savedFieldEntity = fieldService.save(seasonId, fieldEntity);
    return new IdDTO(savedFieldEntity.getId());
  }

}