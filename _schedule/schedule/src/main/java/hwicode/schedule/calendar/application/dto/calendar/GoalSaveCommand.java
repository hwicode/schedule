package hwicode.schedule.calendar.application.dto.calendar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GoalSaveCommand {

    private final Long userId;
    private final String name;
    private final List<YearMonth> yearMonths;
}
