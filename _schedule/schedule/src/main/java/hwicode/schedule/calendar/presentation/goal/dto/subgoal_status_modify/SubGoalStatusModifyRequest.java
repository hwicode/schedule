package hwicode.schedule.calendar.presentation.goal.dto.subgoal_status_modify;

import hwicode.schedule.calendar.domain.SubGoalStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubGoalStatusModifyRequest {

    @NotBlank
    private String subGoalName;

    @NotNull
    private SubGoalStatus subGoalStatus;
}
