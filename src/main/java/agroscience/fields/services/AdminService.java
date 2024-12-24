package agroscience.fields.services;

import agroscience.fields.dao.entities.Crop;
import agroscience.fields.dao.repositories.CropsRepository;
import agroscience.fields.exceptions.DuplicateException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final CropsRepository cropsRepository;

  private boolean validateName(String name) {
    if (name == null || name.isBlank() || cropsRepository.existsByCropName(name)) {
      throw new DuplicateException("Crop with name " + name + " already exists", "name");
    }
    return true;
  }

  @Transactional
  public Crop createCrop(Crop crop) {
    validateName(crop.getCropName());
    return cropsRepository.save(crop);
  }

  @Transactional
  public Crop updateCrop(Long id, Crop newCrop) {
    var crop = cropsRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Field with id " + id + " not found"));
    crop.setCropName(newCrop.getCropName());
    if (!Objects.equals(newCrop.getCropName(), crop.getCropName())) {
      validateName(newCrop.getCropName());
    }
    return cropsRepository.save(crop);
  }

  @Transactional
  public void deleteCropById(Long id) {
    Crop crop = cropsRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Field with id " + id + " not found"));

    for (var cr : crop.getCropRotations()) {
      cr.getField().getCropRotations().remove(cr);
      cr.setField(null);
    }

    cropsRepository.delete(crop);
  }

  public Crop getCrop(Long cropId) {
    return cropsRepository.findById(cropId)
            .orElseThrow(() -> new EntityNotFoundException("Field with id " + cropId + " not found"));
  }

}
