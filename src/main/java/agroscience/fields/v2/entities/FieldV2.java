package agroscience.fields.v2.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "fields")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FieldV2 extends AbstractEntity {

  @Column(name = "name", length = 20, nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "season_id")
  private Season season;

  @OneToMany(mappedBy = "field", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
          orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<Contour> contours;

  @Override
  public void archive() {
    super.archive();
    contours.forEach(Contour::archive);
  }

}