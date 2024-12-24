package agroscience.fields.dao.repositories;

import agroscience.fields.dao.entities.CropRotation;
import agroscience.fields.dao.models.FandCRandC;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CropRotationRepository extends JpaRepository<CropRotation, Long> {

  Slice<CropRotation> findAllByFieldFieldId(Long fieldId, Pageable pageable);

  @Query("SELECT c FROM CropRotation c")
  Slice<CropRotation> findAllAsSliceByOrgId(Long orgId, Pageable pageable);

  @Query("""
          SELECT cr FROM CropRotation cr
          WHERE cr.field.fieldId = :fieldId
          AND cr.cropRotationStartDate = (
            SELECT MAX(cr2.cropRotationStartDate) FROM CropRotation cr2 WHERE cr2.field.fieldId = :fieldId
          )
          """)
  CropRotation findLatestCR(Long fieldId);

  @Query("""
          SELECT f as field, cr as cropRotation, c as crop
          FROM CropRotation cr
          LEFT JOIN cr.field f
          LEFT JOIN cr.crop c
          WHERE cr.cropRotationId = :id
          """)
  FandCRandC findCropRotationById(Long id);

  @Query("""
          SELECT f as field, cr as cropRotation, c as crop
          FROM CropRotation cr
          LEFT JOIN cr.field f
          LEFT JOIN cr.crop c
          WHERE f.fieldOrganizationId = :orgId
          ORDER BY cr.cropRotationStartDate DESC
          """)
  Slice<FandCRandC> findAllByOrgId(Long orgId, Pageable pageable);

}
