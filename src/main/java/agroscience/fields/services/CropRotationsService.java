package agroscience.fields.services;

import agroscience.fields.dao.entities.CropRotation;
import agroscience.fields.dao.models.FandCRandC;
import agroscience.fields.dao.models.FandCRandCImpl;
import agroscience.fields.dao.repositories.CropRotationRepository;
import agroscience.fields.dao.repositories.CropsRepository;
import agroscience.fields.dao.repositories.FieldRepository;
import agroscience.fields.dao.repositories.JbdcDao;
import agroscience.fields.dto.croprotation.ResponseCRWithField;
import agroscience.fields.exceptions.AuthException;
import agroscience.fields.mappers.CropRotationMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CropRotationsService {

  private final CropRotationRepository crRepository;
  private final CropsRepository cropsRepository;
  private final CropRotationMapper crMapper;
  private final FieldRepository fieldRepository;
  private final JbdcDao dao;

  public List<CropRotation> getAllByFieldId(Long orgId, Long fieldId, PageRequest request) {
    if (!fieldRepository.existsById(fieldId)) {
      throw new EntityNotFoundException("Field with id " + fieldId + " not found");
    }
    log.info("Get orgId");
    var fieldOrgId = dao.getOrgIdByFieldId(fieldId);
    if (!Objects.equals(fieldOrgId, orgId)) {
      throw new AuthException("You do not belong to an organization with id " + fieldOrgId);

    }
    return crRepository.findAllByFieldFieldId(fieldId, request).toList();
  }

  public List<FandCRandC> getAll(Long orgId, PageRequest request) {
    return crRepository.findAllByOrgId(orgId, request).toList();
  }

  @Transactional
  public ResponseCRWithField createCR(Long orgId, CropRotation cropRotation, Long cropId, Long fieldId) {
    var crop = cropsRepository.findById(cropId)
            .orElseThrow(() -> new EntityNotFoundException("Crop with id " + cropId + " not found"));

    var field = fieldRepository.findById(fieldId)
            .orElseThrow(() -> new EntityNotFoundException("Field with id " + fieldId + " not found"));

    if (!Objects.equals(field.getFieldOrganizationId(), orgId)) {
      throw new AuthException("You do not belong to an organization with id " + field.getFieldOrganizationId());
    }

    cropRotation.setCrop(crop);
    cropRotation.setField(field);
    crRepository.save(cropRotation);

    return crMapper.map(new FandCRandCImpl(field, cropRotation, crop));
  }

  public ResponseCRWithField getCR(Long id, Long orgId) {
    var fandCRandC = crRepository.findCropRotationById(id);

    Optional.ofNullable(fandCRandC).orElseThrow(
            () -> new EntityNotFoundException("Crop rotation with id " + id + " not found")
    );

    var cropRotationOrgId = fandCRandC.getField().getFieldOrganizationId();

    if (!Objects.equals(cropRotationOrgId, orgId)) {
      throw new AuthException("You do not belong to an organization with с id " + cropRotationOrgId);
    }

    return crMapper.map(fandCRandC);
  }

  @Transactional
  public ResponseCRWithField updateCR(Long orgId, Long id, CropRotation newCropRotation, Long cropId) {
    if (id == null) {
      throw new EntityNotFoundException("Crop rotation with id " + id + " not found");
    }
    var cropRotation = crRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Crop rotation with id " + id + " not found"));

    var cropRotationOrgId = cropRotation.getField().getFieldOrganizationId();

    if (!Objects.equals(cropRotationOrgId, orgId)) {
      throw new AuthException("You do not belong to an organization with id " + cropRotationOrgId);
    }

    var newCrop = cropsRepository
            .findById(cropId).orElseThrow(() -> new EntityNotFoundException("Crop with id " + id + " not found"));
    crMapper.newCRToCR(cropRotation, newCropRotation);
    cropRotation.setCrop(newCrop);
    return crMapper.map(
            new FandCRandCImpl(cropRotation.getField(),
                    crRepository.save(cropRotation),
                    newCrop)
    );
  }

  @Transactional
  public void deleteCR(Long id, Long orgId) {
    var cropRotation = crRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Crop rotation with id " + id + " not found"));

    var cropRotationOrgId = cropRotation.getField().getFieldOrganizationId();

    if (!Objects.equals(cropRotationOrgId, orgId)) {
      throw new AuthException("You do not belong to an organization with id " + cropRotationOrgId);
    }

    var crop = cropRotation.getCrop();
    var field = cropRotation.getField();
    crop.getCropRotations().remove(cropRotation);
    cropRotation.setCrop(null);
    field.getCropRotations().remove(cropRotation);
    cropRotation.setField(null);

    crRepository.delete(cropRotation);
  }

}
