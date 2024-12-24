package agroscience.fields.v2.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "seasons")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Season extends AbstractEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "organization_id")
  private UUID organizationId;

  @OneToMany(mappedBy = "season", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
          orphanRemoval = true)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<FieldV2> fields;

  @Override
  public void archive() {
    super.archive();
    fields.forEach(FieldV2::archive);
  }

}
