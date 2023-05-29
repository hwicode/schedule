package hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubGoalNameModifyRequest {

    @NotBlank
    private String subGoalName;

    @NotBlank
    private String newSubGoalName;
}
