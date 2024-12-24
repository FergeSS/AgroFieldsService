package agroscience.fields.exceptions;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.util.Pair;

@EqualsAndHashCode(callSuper = true)
@Data
public class DateException extends IllegalArgumentException {

  private final List<Pair<String, String>> fieldErrors = new ArrayList<>();

}
