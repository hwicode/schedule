package hwicode.schedule.calendar.application.dto.goal;

import hwicode.schedule.calendar.domain.SubGoalStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubGoalModifyStatusCommand {

    private final Long userId;
    private final Long goalId;
    private final String name;
    private final SubGoalStatus subGoalStatus;
}
