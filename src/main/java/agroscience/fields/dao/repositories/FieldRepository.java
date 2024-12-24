package agroscience.fields.dao.repositories;

import agroscience.fields.dao.entities.Field;
import agroscience.fields.dao.models.FieldAndCurrentCrop;
import agroscience.fields.dao.models.FieldCRsSoil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FieldRepository extends JpaRepository<Field, Long> {

  @Query("""
          SELECT f as field, cr as cropRotation
          FROM Field f
          LEFT JOIN FETCH f.cropRotations cr
          WHERE (cr.cropRotationStartDate = (SELECT MAX(cr2.cropRotationStartDate)
                                FROM CropRotation cr2
                                WHERE cr2.field = f)
                 OR cr.cropRotationStartDate IS NULL)
          AND f.fieldOrganizationId = :orgId
          """)
  Slice<FieldAndCurrentCrop> fieldsWithLatestCrops(@Param("orgId") Long orgId, Pageable pageable);

  @Query("""
          SELECT f as field, cr as cropRotation
          FROM Field f
          LEFT JOIN FETCH f.cropRotations cr
          WHERE (cr.cropRotationStartDate = (SELECT MAX(cr2.cropRotationStartDate)
                                FROM CropRotation cr2
                                WHERE cr2.field = f)
                 OR cr.cropRotationStartDate IS NULL)
          AND f.fieldId = :fieldId
          """)
  FieldAndCurrentCrop fieldWithLatestCrop(@Param("fieldId") Long fieldId);

  @Query("""
          SELECT f as field, cr as cropRotation, s as soil, c as crop
          FROM Field f
          LEFT JOIN FETCH f.cropRotations cr
          LEFT JOIN f.soils s
          LEFT JOIN cr.crop c
          WHERE f.fieldId = :fieldId
          ORDER BY cr.cropRotationStartDate DESC, s.soilSampleDate DESC
          LIMIT 1
          """)
  FieldCRsSoil getFullField(@Param("fieldId") Long fieldId);

  boolean existsByFieldName(String name);

}
