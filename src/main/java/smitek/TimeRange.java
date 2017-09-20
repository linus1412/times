package smitek;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@Data
@Builder
@EqualsAndHashCode
public class TimeRange {

  private LocalTime pickUp;
  private LocalTime dropOff;

}
