package hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubGoalNameModifyResponse {

    private Long goalId;
    private String newSubGoalName;
}
