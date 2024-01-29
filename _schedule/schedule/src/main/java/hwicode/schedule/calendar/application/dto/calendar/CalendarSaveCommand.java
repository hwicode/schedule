package hwicode.schedule.calendar.application.dto.calendar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;

@Getter
@RequiredArgsConstructor
public class CalendarSaveCommand {

    private final Long userId;
    private final YearMonth yearMonth;
}
