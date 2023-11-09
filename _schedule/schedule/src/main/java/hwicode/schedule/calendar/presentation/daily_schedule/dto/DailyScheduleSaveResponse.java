package hwicode.schedule.calendar.presentation.daily_schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyScheduleSaveResponse {

    private Long dailyScheduleId;
    private LocalDate date;
}
