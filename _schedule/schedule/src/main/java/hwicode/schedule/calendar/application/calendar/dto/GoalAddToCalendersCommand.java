package hwicode.schedule.calendar.application.calendar.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GoalAddToCalendersCommand {

    private final Long userId;
    private final Long goalId;
    private final List<YearMonth> yearMonths;
}
