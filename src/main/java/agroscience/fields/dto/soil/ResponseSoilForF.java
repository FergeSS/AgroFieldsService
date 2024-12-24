package agroscience.fields.dto.soil;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseSoilForF {

  @JsonProperty("id")
  private Long soilId;
  @JsonProperty("ph")
  private String soilPh;
  @JsonProperty("sampleDate")
  private String soilSampleDate;
  @JsonProperty("organicMatter")
  private String soilOrganicMatter;
  @JsonProperty("mobileP")
  private String soilMobileP;
  @JsonProperty("mobileK")
  private String soilMobileK;
  @JsonProperty("mobileS")
  private String soilMobileS;
  @JsonProperty("nitrateN")
  private String soilNitrateN;
  @JsonProperty("ammoniumN")
  private String soilAmmoniumN;
  @JsonProperty("hydrolyticAcidity")
  private String soilHydrolyticAcidity;
  @JsonProperty("caExchange")
  private String soilCaExchange;
  @JsonProperty("mgExchange")
  private String soilMgExchange;
  @JsonProperty("b")
  private String soilB;
  @JsonProperty("co")
  private String soilCo;
  @JsonProperty("mn")
  private String soilMn;
  @JsonProperty("zn")
  private String soilZn;

}
