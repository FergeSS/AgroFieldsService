package agroscience.fields.dto.soil;

import agroscience.fields.exceptions.validation.constraints.LocalDateFormat;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestSoil {

  @NotNull
  private Long fieldId;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("ph")
  private String soilPh;

  @Size(max = 10, message = "Maximum 10 characters")
  @Pattern(regexp = "^(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])-(\\d{4})$",
          message = "Date entered incorrectly. Must be dd-mm-yyyy.")
  @LocalDateFormat
  @JsonAlias("sampleDate")
  private String soilSampleDate;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("organicMatter")
  private String soilOrganicMatter;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("mobileP")
  private String soilMobileP;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("mobileK")
  private String soilMobileK;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("mobileS")
  private String soilMobileS;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("nitrateN")
  private String soilNitrateN;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("ammoniumN")
  private String soilAmmoniumN;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("hydrolyticAcidity")
  private String soilHydrolyticAcidity;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("caExchange")
  private String soilCaExchange;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("mgExchange")
  private String soilMgExchange;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("b")
  private String soilB;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("co")
  private String soilCo;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("mn")
  private String soilMn;

  @Size(max = 10, message = "Maximum 10 characters")
  @JsonAlias("zn")
  private String soilZn;

}
