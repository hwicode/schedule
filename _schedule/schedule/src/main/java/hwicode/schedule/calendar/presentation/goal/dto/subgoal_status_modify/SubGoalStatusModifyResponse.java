package hwicode.schedule.calendar.presentation.goal.dto.subgoal_status_modify;

import hwicode.schedule.calendar.domain.GoalStatus;
import hwicode.schedule.calendar.domain.SubGoalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubGoalStatusModifyResponse {

    private String subGoalName;
    private GoalStatus modifiedGoalStatus;
    private SubGoalStatus modifiedSubGoalStatus;
}
