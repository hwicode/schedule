package hwicode.schedule.calendar.presentation.calendar.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class CalendarSaveResponse {

    private Long calendarId;
    private YearMonth yearMonth;
}
