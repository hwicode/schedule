package hwicode.schedule.calendar.presentation.goal.dto.subgoal_delete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubGoalDeleteRequest {

    @NotBlank
    private String subGoalName;
}
