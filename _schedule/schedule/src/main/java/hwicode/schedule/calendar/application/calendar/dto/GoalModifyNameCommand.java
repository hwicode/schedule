package hwicode.schedule.calendar.application.calendar.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;

@Getter
@RequiredArgsConstructor
public class GoalModifyNameCommand {

    private final Long userId;
    private final YearMonth yearMonth;
    private final String name;
    private final String newName;
}
