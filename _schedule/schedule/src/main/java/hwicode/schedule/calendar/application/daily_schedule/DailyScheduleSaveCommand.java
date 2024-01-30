package hwicode.schedule.calendar.application.daily_schedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class DailyScheduleSaveCommand {

    private final Long userId;
    private final LocalDate date;
}
