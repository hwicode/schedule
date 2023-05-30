package hwicode.schedule.calendar.presentation.goal.dto.goal_status_modify;

import hwicode.schedule.calendar.domain.GoalStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GoalStatusModifyRequest {

    @NotNull
    private GoalStatus goalStatus;
}
