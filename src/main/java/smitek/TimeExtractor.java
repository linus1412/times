package smitek;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeExtractor {

  public Map<DayOfWeek, List<TimeRange>> convert(String days, String opens, String closes) {

    final String[] daysBits = days.split(",");
    final String[] opensBits = opens.split(",");
    final String[] closesBits = closes.split(",");

    if (!(daysBits.length == opensBits.length && opensBits.length == closesBits.length)) {
      throw new IllegalArgumentException("days, opens and closes parameters must contain the same number of comma separated values");
    }

    Map<DayOfWeek, List<TimeRange>> dayTimes = new HashMap<>();

    try {

      for (int i = 0; i < daysBits.length; i++) {
        int dayInt = Integer.parseInt(daysBits[i]) + 1;
        final DayOfWeek day = DayOfWeek.of(dayInt);
        dayTimes.computeIfAbsent(day, dayOfWeek -> new ArrayList<>())
          .add(
            TimeRange.builder()
              .pickUp(LocalTime.parse(opensBits[i]))
              .dropOff(LocalTime.parse(closesBits[i]))
              .build()
          );
      }
    } catch (NumberFormatException | DateTimeException  e) {
      throw new TimeExtractorException(e);
    }

    return dayTimes;
  }
}
