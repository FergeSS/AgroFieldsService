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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Geometry;

@Entity
@Table(name = "field")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Field {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crop_rotation_crop_rotation_id_seq")
  @SequenceGenerator(
          name = "crop_rotation_crop_rotation_id_seq",
          sequenceName = "crop_rotation_crop_rotation_id_seq",
          allocationSize = 1
  )
  @Column(name = "field_id")
  private Long fieldId;

  @Column(name = "field_organization_id", nullable = false)
  private Long fieldOrganizationId;

  @Column(name = "field_name", length = 100, nullable = false, unique = true)
  private String fieldName;

  @Column(name = "field_square_area", length = 40, nullable = false)
  private String fieldSquareArea;

  @Column(name = "field_geom", nullable = false, columnDefinition = "geometry(Geometry,0)")
  private Geometry fieldGeom; // Тип Geometry для хранения геометрических данных

  @Column(name = "field_description", length = 256)
  private String fieldDescription;

  @Column(name = "field_color", length = 6, nullable = false)
  private String fieldColor;

  @Column(name = "field_activity_start")
  private LocalDate fieldActivityStart;

  @Column(name = "field_activity_end")
  private LocalDate fieldActivityEnd;

  @OneToMany(mappedBy = "field", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
          fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<CropRotation> cropRotations = new ArrayList<>();

  @OneToMany(mappedBy = "field", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
          fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<Soil> soils = new ArrayList<>();

}
