package agroscience.fields.dao.repositories;

import agroscience.fields.dao.entities.Crop;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropsRepository extends JpaRepository<Crop, Long> {

  Crop findCropByCropId(Long cropId);

  boolean existsByCropName(String name);

  List<Crop> findAllByCropNameIgnoreCaseStartingWith(String cropName, PageRequest of);

}
