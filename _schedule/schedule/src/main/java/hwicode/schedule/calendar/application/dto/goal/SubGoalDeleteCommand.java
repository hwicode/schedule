package hwicode.schedule.calendar.application.dto.goal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubGoalDeleteCommand {

    private final Long userId;
    private final Long goalId;
    private final String name;
}
