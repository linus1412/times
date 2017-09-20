package smitek;

import org.junit.Before;
import org.junit.Test;
import smitek.TimeExtractor;
import smitek.TimeExtractorException;
import smitek.TimeRange;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.TUESDAY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TimeExtractorTest {

  private Map<DayOfWeek, List<TimeRange>> hours;

  @Before
  public void before() {
    String days = "0,0,1,1,2,2,3,3,4,4,5,5,6";
    String opens =  "01:00,03:00,02:00,04:00,03:00,05:00,04:00,06:00,05:00,07:00,06:00,08:00,10:00";
    String closes = "02:00,04:00,03:00,05:00,04:00,06:00,05:00,07:00,06:00,08:00,07:00,09:00,12:00";
    hours = new TimeExtractor().convert(days, opens, closes);
  }

  @Test
  public void monday() {
    final List<TimeRange> mondayHours = hours.get(MONDAY);
    assertHasRanges(mondayHours, 2);

    final TimeRange morning = mondayHours.get(0);
    assertRange(morning, 1, 0, 2, 0);

    final TimeRange afternoon = mondayHours.get(1);
    assertRange(afternoon, 3, 0, 4, 0);
  }


  @Test
  public void tuesday() {
    final List<TimeRange> tuesdayHours = hours.get(TUESDAY);
    assertHasRanges(tuesdayHours, 2);

    final TimeRange morning = tuesdayHours.get(0);
    assertRange(morning, 2, 0, 3, 0);

    final TimeRange afternoon = tuesdayHours.get(1);
    assertRange(afternoon, 4, 0, 5, 0);
  }

  @Test
  public void sunday() {
    final List<TimeRange> sundayHours = hours.get(SUNDAY);
    assertHasRanges(sundayHours, 1);

    final TimeRange range = sundayHours.get(0);
    assertRange(range, 10, 0, 12, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void dataInvalid() {
    hours = new TimeExtractor().convert("0,1,2", "09:00", "10:00,10:00,10:00");
  }

  @Test(expected = TimeExtractorException.class)
  public void dataInvalidDay() {
    hours = new TimeExtractor().convert("7", "09:00", "10:00");
  }

  @Test(expected = TimeExtractorException.class)
  public void dataInvalidTime() {
    hours = new TimeExtractor().convert("0", "AA:BB", "10:00");
  }

  private void assertRange(TimeRange timeRange, int pickUpHour, int pickUpMin, int dropOfHour, int dropOfMin) {
    assertThat(timeRange, is(TimeRange.builder()
      .pickUp(LocalTime.of(pickUpHour, pickUpMin))
      .dropOff(LocalTime.of(dropOfHour, dropOfMin))
      .build()));
  }

  private void assertHasRanges(List<TimeRange> sundayHours, int ranges) {
    assertThat(sundayHours.size(), is(ranges));
  }

}