package agroscience.fields.services;

import agroscience.fields.dao.entities.Soil;
import agroscience.fields.dao.repositories.FieldRepository;
import agroscience.fields.dao.repositories.SoilRepository;
import agroscience.fields.exceptions.AuthException;
import agroscience.fields.mappers.SoilMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SoilService {

  private final SoilRepository soilRepository;
  private final FieldRepository fieldRepository;
  private final SoilMapper soilMapper;

  @Transactional
  public Soil createSoil(Long orgId, Soil soil, Long fieldId) {
    var field = fieldRepository.findById(fieldId)
            .orElseThrow(() -> new EntityNotFoundException("Field with id " + fieldId + " not found"));

    if (!Objects.equals(field.getFieldOrganizationId(), orgId)) {
      throw new AuthException("You do not belong to an organization with id " + orgId);
    }
    soil.setField(field);
    return soilRepository.save(soil);
  }

  @Transactional
  public void deleteSoilById(Long soilId, Long orgId) {
    var soil = soilRepository.findById(soilId)
            .orElseThrow(() -> new EntityNotFoundException("Soil with id " + soilId + " not found"));
    var field = soil.getField();
    if (!Objects.equals(orgId, field.getFieldOrganizationId())) {
      throw new AuthException("You do not belong to an organization with id " + orgId);
    }

    field.getSoils().remove(soil);
    soil.setField(null);

    soilRepository.delete(soil);
  }

  @Transactional
  public Soil updateSoil(Long orgId, Long soilId, Soil newSoil) {
    var soil = soilRepository.findById(soilId)
            .orElseThrow(() -> new EntityNotFoundException("Soil with id " + soilId + " not found"));

    var orgIdBySoilId = soil.getField().getFieldOrganizationId();

    if (!Objects.equals(orgId, orgIdBySoilId)) {
      throw new AuthException("You do not belong to an organization with id " + orgId);
    }
    soilMapper.map(soil, newSoil);
    return soilRepository.save(soil);
  }

}
