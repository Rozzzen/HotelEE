package util;

import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateUtil {

    private static final Logger LOG = Logger.getLogger(DateUtil.class);

    public LocalDate validateDate(int day, int month, int year) throws DateTimeParseException {

        String dateStr = year + "-";
        if(month < 10) dateStr += "0" + month;
        dateStr += "-" + day;
        LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));
        return LocalDate.of(year, month, day);

    }
}
