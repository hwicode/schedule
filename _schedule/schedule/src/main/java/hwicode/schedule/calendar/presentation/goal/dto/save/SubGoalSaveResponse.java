package hwicode.schedule.calendar.presentation.goal.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubGoalSaveResponse {

    private Long goalId;
    private String subGoalName;
}
