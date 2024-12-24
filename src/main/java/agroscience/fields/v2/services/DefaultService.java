package agroscience.fields.v2.services;

import static java.lang.String.format;

import agroscience.fields.v2.entities.AbstractEntity;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DefaultService {

  private static final String NOT_FOUND = "Entity with id %s not found";

  protected static  <T> T getOrThrow(UUID id, Function<UUID, Optional<T>> function) {
    return function.apply(id).orElseThrow(() -> new EntityNotFoundException(format(NOT_FOUND, id)));
  }

  protected static <T extends AbstractEntity> void checkArchived(UUID id, T entity) {
    if (entity.isArchived()) {
      throw new EntityNotFoundException(format(NOT_FOUND + " because archived", id));
    }
  }

}