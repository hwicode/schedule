package hwicode.schedule.calendar.application.dto.daily_schedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class DailyScheduleProvideCommand {

    private final Long userId;
    private final LocalDate date;
    private final LocalDate now;
}
