package agroscience.fields.v2.mappers;

import agroscience.fields.v2.entities.Season;
import generated.agroscience.fields.api.model.SeasonBaseDTO;
import generated.agroscience.fields.api.model.SeasonWithFieldsDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeasonMapper {

  Season map(SeasonBaseDTO seasonDTO);

  List<SeasonBaseDTO> map(List<Season> all);

  List<SeasonWithFieldsDTO> mapWithField(List<Season> all);

}