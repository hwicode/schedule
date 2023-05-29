package hwicode.schedule.calendar.presentation.goal;

import hwicode.schedule.calendar.application.GoalAggregateService;
import hwicode.schedule.calendar.presentation.goal.dto.save.SubGoalSaveRequest;
import hwicode.schedule.calendar.presentation.goal.dto.save.SubGoalSaveResponse;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify.SubGoalNameModifyRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify.SubGoalNameModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class GoalController {

    private final GoalAggregateService goalAggregateService;

    @PostMapping("/goals/{goalId}/sub-goals")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubGoalSaveResponse saveSubGoal(@PathVariable @Positive Long goalId,
                                           @RequestBody @Valid SubGoalSaveRequest subGoalSaveRequest) {
        Long subGoalId = goalAggregateService.saveSubGoal(
                goalId, subGoalSaveRequest.getSubGoalName()
        );
        return new SubGoalSaveResponse(subGoalId, subGoalSaveRequest.getSubGoalName());
    }

    @PatchMapping("/goals/{goalId}/sub-goals/{subGoalId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public SubGoalNameModifyResponse changeSubGoalName(@PathVariable @Positive Long goalId,
                                                       @RequestBody @Valid SubGoalNameModifyRequest subGoalNameModifyRequest) {
        String changedSubGoalName = goalAggregateService.changeSubGoalName(
                goalId, subGoalNameModifyRequest.getSubGoalName(), subGoalNameModifyRequest.getNewSubGoalName()
        );
        return new SubGoalNameModifyResponse(goalId, changedSubGoalName);
    }

}
