package agroscience.fields.dao.repositories;

import agroscience.fields.dao.entities.Soil;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoilRepository extends JpaRepository<Soil, Long> {

  boolean existsBySoilSampleDate(LocalDate date);

}
