package hwicode.schedule.calendar.presentation.calendar.dto.goal_name_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GoalNameModifyRequest {

    @NotNull
    private YearMonth yearMonth;

    @NotBlank
    private String goalName;

    @NotBlank
    private String newGoalName;
}
