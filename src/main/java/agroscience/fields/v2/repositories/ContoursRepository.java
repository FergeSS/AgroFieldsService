package agroscience.fields.v2.repositories;

import agroscience.fields.v2.entities.Contour;
import agroscience.fields.v2.entities.FieldV2;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ContoursRepository extends AbstractRepository<Contour> {

  List<Contour> findAllByFieldAndArchivedIsFalse(FieldV2 fieldV2);

}