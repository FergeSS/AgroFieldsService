package agroscience.fields.dao.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "crop")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Crop {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crop_crop_id_seq")
  @SequenceGenerator(name = "crop_crop_id_seq", sequenceName = "crop_crop_id_seq", allocationSize = 1)
  @Column(name = "crop_id")
  private Long cropId;

  @Column(name = "crop_name", length = 50, nullable = false, unique = true)
  private String cropName;

  @OneToMany(mappedBy = "crop", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
          fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<CropRotation> cropRotations = new ArrayList<>();

}
