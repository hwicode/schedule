package hwicode.schedule.calendar.presentation.goal.dto.goal_status_modify;

import hwicode.schedule.calendar.domain.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalStatusModifyResponse {

    private Long goalId;
    private GoalStatus goalStatus;
}
