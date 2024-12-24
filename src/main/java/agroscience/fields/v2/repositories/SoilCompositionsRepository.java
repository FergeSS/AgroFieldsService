package agroscience.fields.v2.repositories;

import agroscience.fields.v2.entities.Contour;
import agroscience.fields.v2.entities.SoilComposition;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public interface SoilCompositionsRepository extends AbstractRepository<SoilComposition> {

  List<SoilComposition> findAllByContourAndArchivedIsFalse(Contour contour, Sort sort);

}