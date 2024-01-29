package hwicode.schedule.calendar.application.dto.goal;

import hwicode.schedule.calendar.domain.GoalStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoalModifyStatusCommand {

    private final Long userId;
    private final Long goalId;
    private final GoalStatus goalStatus;
}
