package hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubGoalNameModifyResponse {

    private Long goalId;
    private String newSubGoalName;
}
