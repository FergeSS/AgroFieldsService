package agroscience.fields.v2.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Geometry;

@Entity
@Table(name = "contours")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Contour extends AbstractEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "square_area", length = 40, nullable = false)
  private String squareArea;

  @Column(name = "geom", nullable = false, columnDefinition = "geometry(Geometry,0)")
  private Geometry geom; // Тип Geometry для хранения геометрических данных

  @Column(name = "color", length = 6, nullable = false)
  private String color;

  @ManyToOne
  @JoinColumn(name = "field_id")
  private FieldV2 field;

  @OneToMany(mappedBy = "contour", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
          orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OrderBy("startDate ASC")
  private List<CropRotationV2> cropRotations;

  @OneToMany(mappedBy = "contour", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
          orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<SoilComposition> soilCompositions;

  @Override
  public void archive() {
    super.archive();
    soilCompositions.forEach(SoilComposition::archive);
    cropRotations.forEach(CropRotationV2::archive);
    List<Contour> contoursWithArchivedFalse = field.getContours().stream()
            .filter(contour -> !contour.isArchived())
            .toList();
    if (contoursWithArchivedFalse.isEmpty()) {
      field.setArchived(true);
    }
  }

}
