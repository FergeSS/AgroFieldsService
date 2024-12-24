package agroscience.fields.v2.controllers;

import agroscience.fields.v2.entities.SoilComposition;
import agroscience.fields.v2.mappers.SoilCompositionMapper;
import agroscience.fields.v2.services.ContoursService;
import agroscience.fields.v2.services.SoilCompositionsService;
import generated.agroscience.fields.api.SoilCompositionsApi;
import generated.agroscience.fields.api.model.IdDTO;
import generated.agroscience.fields.api.model.SoilCompositionDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('organization') or hasRole('worker')")
public class SoilCompositionsController implements SoilCompositionsApi {

  private final SoilCompositionMapper soilCompositionMapper;
  private final SoilCompositionsService soilCompositionsService;

  @Override
  public void changeSoilComposition(UUID soilCompositionId, SoilCompositionDTO soilCompositionDTO) {
    SoilComposition updatedSoilComposition = soilCompositionMapper.map(soilCompositionDTO);
    soilCompositionsService.updateSoilComposition(soilCompositionId, updatedSoilComposition);
  }

  @Override
  public void deleteSoilComposition(UUID soilCompositionId) {
    soilCompositionsService.archive(soilCompositionId);
  }

  @Override
  public List<SoilCompositionDTO> getSoilCompositions(UUID contourId) {
    List<SoilComposition> soilCompositionList = soilCompositionsService.getAllSoilCompositions(contourId);
    return soilCompositionMapper.map(soilCompositionList);
  }

  @Override
  public IdDTO saveSoilComposition(UUID contourId, SoilCompositionDTO soilCompositionDTO) {
    var soilComposition = soilCompositionMapper.map(soilCompositionDTO);
    var soilCompositionEntity = soilCompositionsService.save(contourId, soilComposition);
    return new IdDTO(soilCompositionEntity.getId());
  }

}