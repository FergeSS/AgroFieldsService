package agroscience.fields.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateConverting {

  static String stringFormat = "dd-MM-yyyy";

  public static LocalDate stringToLocalDate(String date) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(stringFormat);
    dateFormat.setLenient(false);
    return dateFormat
            .parse(date).toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
  }

  public static String localDateToString(LocalDate localDate) {
    return localDate.format(DateTimeFormatter.ofPattern(stringFormat));
  }

}
