package agroscience.fields.v2.repositories;

import agroscience.fields.v2.entities.FieldV2;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldsRepository extends AbstractRepository<FieldV2> {

  List<FieldV2> findAllBySeasonIdAndArchivedIsFalse(UUID id);

  List<FieldV2> findAllByArchivedIsFalse();

}