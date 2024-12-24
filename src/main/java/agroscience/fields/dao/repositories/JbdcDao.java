package agroscience.fields.dao.repositories;

import agroscience.fields.dto.field.CoordinatesWithFieldId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JbdcDao {

  private static final String GET_ORG_ID_BY_FIELD_ID = """
          SELECT f.field_organization_id as orgId 
          FROM field f
          WHERE f.field_id = ?
          """;
  private static final String GET_ALL_COORDINATES = """
          SELECT 
              field_id, 
              ST_X(ST_Centroid(field_geom)) as longitude, 
              ST_Y(ST_Centroid(field_geom)) as latitude 
          FROM field
          """;
  private final JdbcTemplate jdbcTemplate;

  public List<CoordinatesWithFieldId> getAllCoordinates() {

    return jdbcTemplate.query(
            GET_ALL_COORDINATES,
            (rs, rowNum) -> new CoordinatesWithFieldId(rs.getLong("field_id"),
                    rs.getDouble("longitude"),
                    rs.getDouble("latitude"))
    );
  }

  public Long getOrgIdByFieldId(Long fieldId) {
    return jdbcTemplate.queryForObject(
            GET_ORG_ID_BY_FIELD_ID,
            Long.class,
            fieldId
    );
  }

}
