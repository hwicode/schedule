package hwicode.schedule.calendar.presentation.calendar.dto.goal_name_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalNameModifyResponse {

    private Long calendarId;
    private String newGoalName;
}
