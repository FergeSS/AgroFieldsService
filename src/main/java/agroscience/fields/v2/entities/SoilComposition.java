package agroscience.fields.v2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

@Entity
@Table(name = "soil_compositions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("MemberName")
@EqualsAndHashCode(callSuper = true)
public class SoilComposition extends AbstractEntity {

  private static GeometryFactory geometryFactory = new GeometryFactory();

  @Column(name = "ph")
  private String ph;

  @Column(name = "sample_date")
  private LocalDate sampleDate;

  @Column(name = "organic_matter")
  private String organicMatter;

  @Column(name = "mobile_p")
  private String mobileP;

  @Column(name = "mobile_k")
  private String mobileK;

  @Column(name = "mobile_s")
  private String mobileS;

  @Column(name = "nitrate_n")
  private String nitrateN;

  @Column(name = "ammonium_n")
  private String ammoniumN;

  @Column(name = "hydrolytic_acidity")
  private String hydrolyticAcidity;

  @Column(name = "ca_exchange")
  private String caExchange;

  @Column(name = "mg_exchange")
  private String mgExchange;

  @Column(name = "b")
  private String b;

  @Column(name = "co")
  private String co;

  @Column(name = "mn")
  private String mn;

  @Column(name = "zn")
  private String zn;

  @Column(name = "coordinates", columnDefinition = "GEOMETRY(Point, 4326)")
  private Geometry coordinates;

  @ManyToOne
  @JoinColumn(name = "contour_id")
  private Contour contour;

  public void setDefaultCoordinates() {
    Coordinate coordinate = contour.getGeom().getCoordinates()[0];
    this.coordinates = geometryFactory.createPoint(coordinate);
  }

  public void checkBelongToContour() {
    if (!contour.getGeom().contains(coordinates)
            && !contour.getGeom().intersects(coordinates)) {
      throw new IllegalArgumentException("The point does not belong to the contour");
    }
  }

}
