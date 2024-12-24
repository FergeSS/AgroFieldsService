package agroscience.fields.dao.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "crop_rotation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropRotation {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crop_rotation_crop_rotation_id_seq")
  @SequenceGenerator(
          name = "crop_rotation_crop_rotation_id_seq",
          sequenceName = "crop_rotation_crop_rotation_id_seq",
          allocationSize = 1
  )
  @Column(name = "crop_rotation_id")
  private Long cropRotationId;

  @JoinColumn(name = "field_id", referencedColumnName = "field_id", nullable = false)
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Field field;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
  @JoinColumn(name = "crop_id", referencedColumnName = "crop_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Crop crop;

  @Column(name = "crop_rotation_start_date", nullable = false)
  private LocalDate cropRotationStartDate;

  @Column(name = "crop_rotation_end_date", nullable = false)
  private LocalDate cropRotationEndDate;

  @Column(name = "crop_rotation_description", length = 256, nullable = false)
  private String cropRotationDescription;

}
