package agroscience.fields.v2.controllers;

import agroscience.fields.v2.entities.Contour;
import agroscience.fields.v2.mappers.ContourMapper;
import agroscience.fields.v2.services.ContoursService;
import generated.agroscience.fields.api.ContoursApi;
import generated.agroscience.fields.api.model.ContourBaseDTO;
import generated.agroscience.fields.api.model.IdDTO;
import generated.agroscience.fields.api.model.UpdateContourDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('organization') or hasRole('worker')")
public class ContoursController implements ContoursApi {

  private final ContoursService contourService;
  private final ContourMapper contourMapper;

  @Override
  public void changeContour(UUID contourId, UpdateContourDTO updateContourDTO) {
    Contour updatedContour = contourMapper.map(updateContourDTO);
    contourService.update(contourId, updatedContour);
  }

  @Override
  public void deleteContour(UUID contourId) {
    contourService.archive(contourId);
  }

  @Override
  public List<ContourBaseDTO> findContours(UUID fieldId) {
    List<Contour> contourList =  contourService.findAllByFieldId(fieldId);
    return contourMapper.map(contourList);
  }

  @Override
  public IdDTO saveContour(UUID fieldId, ContourBaseDTO contourDTO) {
    var contourEntity = contourMapper.map(contourDTO);
    var contourId = contourService.save(fieldId, contourEntity);
    return new IdDTO(contourId);
  }

}