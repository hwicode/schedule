package hwicode.schedule.calendar.presentation.calendar.dto.goal_name_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GoalNameModifyResponse {

    private Long calendarId;
    private String newGoalName;
}
