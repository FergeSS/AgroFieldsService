package agroscience.fields.services;

import agroscience.fields.configuration.MeteoProperties;
import agroscience.fields.dao.entities.CropRotation;
import agroscience.fields.dao.entities.Field;
import agroscience.fields.dao.models.FieldAndCurrentCrop;
import agroscience.fields.dao.models.FieldAndCurrentCropImpl;
import agroscience.fields.dao.repositories.FieldRepository;
import agroscience.fields.dao.repositories.JbdcDao;
import agroscience.fields.dto.ResponseMeteo;
import agroscience.fields.dto.field.CoordinatesWithFieldId;
import agroscience.fields.dto.field.RequestField;
import agroscience.fields.dto.field.ResponseFullField;
import agroscience.fields.exceptions.AuthException;
import agroscience.fields.exceptions.DuplicateException;
import agroscience.fields.mappers.FieldMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FieldService {

  private final FieldRepository fieldRepository;
  private final FieldMapper fieldMapper;
  private final JbdcDao jbdcFieldDao;
  private final RestTemplate restTemplate;
  private final MeteoProperties meteoProperties;

  @Transactional
  public FieldAndCurrentCrop createField(Field field) {
    return new FieldAndCurrentCropImpl(fieldRepository.save(field), new CropRotation());
  }

  public ResponseFullField getFullField(Long id, Long orgId) {
    var fcrsc = fieldRepository.getFullField(id);
    if (fcrsc == null) {
      throw new EntityNotFoundException("Field with id " + id + " not found");
    } else if (fcrsc.getField() == null) {
      throw new EntityNotFoundException("Field with id " + id + " not found");
    }
    if (!Objects.equals(fcrsc.getField().getFieldOrganizationId(), orgId)) {
      throw new AuthException("You do not belong to an organization with id "
              + fcrsc.getField().getFieldOrganizationId());
    }
    List<ResponseMeteo> meteoList;
    try {
      ResponseEntity<List<ResponseMeteo>> response = restTemplate.exchange(
          "http://" + meteoProperties.getHost() + ":" + meteoProperties.getPort() + "/api/v1/meteo/" + id,
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<>() {
          }
      );

      if (response.getStatusCode().is2xxSuccessful()) {
        meteoList = response.getBody();
      } else {
        throw new RuntimeException("From meteo " + response.getStatusCode());
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      meteoList = null;
    }

    return fieldMapper.map(fcrsc, meteoList);
  }

  public FieldAndCurrentCrop getFieldWithCurrentCrop(Long id, Long orgId) {
    var fieldAndCropRotation = fieldRepository.fieldWithLatestCrop(id);

    if (fieldAndCropRotation == null || fieldAndCropRotation.getField() == null) {
      throw new EntityNotFoundException("Field with id " + id + " not found");
    }
    if (!Objects.equals(fieldAndCropRotation.getField().getFieldOrganizationId(), orgId)) {
      throw new AuthException("You do not belong to an organization with id "
              + fieldAndCropRotation.getField().getFieldOrganizationId());
    }

    return fieldAndCropRotation;
  }

  public List<FieldAndCurrentCrop> getFields(Long orgId, Pageable page) {
    return fieldRepository.fieldsWithLatestCrops(orgId, page).toList();
  }

  @Transactional
  public FieldAndCurrentCrop updateField(Long id, RequestField request, Long orgId) {
    var fieldWithCrop = fieldRepository.fieldWithLatestCrop(id);
    if (fieldWithCrop == null) {
      throw new EntityNotFoundException("Field with id " + id + " not found");
    } else if (fieldWithCrop.getField() == null) {
      throw new EntityNotFoundException("Field with id " + id + " not found");
    }

    if (!Objects.equals(fieldWithCrop.getField().getFieldOrganizationId(), orgId)) {
      throw new AuthException("You do not belong to an organization with id "
              + fieldWithCrop.getField().getFieldOrganizationId());
    }

    var field = fieldWithCrop.getField();
    var nameBefore = field.getFieldName();
    fieldMapper.mapToFieldReview(field, request, orgId);
    try {
      fieldRepository.save(field);
    } catch (DataIntegrityViolationException ex) {
      throw new DuplicateException("Field with name " + field.getFieldName() + " already exists", "name");
    }
    return fieldWithCrop;
  }

  @Transactional
  public void deleteField(Long id, Long orgId) {

    var field = fieldRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Field with id " + id + " not found"));

    if (!Objects.equals(field.getFieldOrganizationId(), orgId)) {
      throw new AuthException("You do not belong to an organization with id " + field.getFieldOrganizationId());
    }

    // Важно удалить у кропов тоже, иначе будет DataIntegrityViolationException
    //Либо удаление вовсе не сработает
    field.getCropRotations().forEach(cr -> cr.getCrop().getCropRotations().remove(cr));
    fieldRepository.delete(field);
  }

  public List<FieldAndCurrentCrop> getFieldsForPreview(Long orgId, Pageable pageable) {
    return fieldRepository.fieldsWithLatestCrops(orgId, pageable).toList();
  }

  public List<CoordinatesWithFieldId> getAllCoordinates() {
    return jbdcFieldDao.getAllCoordinates();
  }

}
