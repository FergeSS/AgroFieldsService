package agroscience.fields.services;

import agroscience.fields.dao.entities.Crop;
import agroscience.fields.dao.repositories.CropsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CropsService {

  private final CropsRepository cropsRepository;

  public List<Crop> getCrop(String name, int page, int size) {
    return cropsRepository.findAllByCropNameIgnoreCaseStartingWith(
            name, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "cropName"))
    );
  }

}