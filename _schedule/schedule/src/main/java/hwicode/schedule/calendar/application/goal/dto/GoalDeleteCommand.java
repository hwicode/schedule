package hwicode.schedule.calendar.application.goal.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoalDeleteCommand {

    private final Long userId;
    private final Long goalId;
}
