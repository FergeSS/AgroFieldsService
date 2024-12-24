package agroscience.fields;

import agroscience.fields.v2.entities.FieldV2;
import java.util.List;
import agroscience.fields.v2.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldTestRepository extends AbstractRepository<FieldV2> {

  @EntityGraph(attributePaths = {"contours"})
  List<FieldV2> findAllByArchivedIsFalse();

}