package hwicode.schedule.calendar.presentation.calendar.dto.calendar_goal;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;
import java.util.List;

@Getter
@AllArgsConstructor
public class GoalAddToCalendarsResponse {

    private Long goalId;
    private List<YearMonth> calendars;
}
