package hwicode.schedule.calendar.presentation.goal;

import hwicode.schedule.calendar.application.goal.GoalAggregateService;
import hwicode.schedule.calendar.application.goal.dto.*;
import hwicode.schedule.calendar.domain.GoalStatus;
import hwicode.schedule.calendar.presentation.goal.dto.goal_status_modify.GoalStatusModifyRequest;
import hwicode.schedule.calendar.presentation.goal.dto.goal_status_modify.GoalStatusModifyResponse;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify.SubGoalNameModifyRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify.SubGoalNameModifyResponse;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_save.SubGoalSaveRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_save.SubGoalSaveResponse;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_status_modify.SubGoalStatusModifyRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_status_modify.SubGoalStatusModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class GoalController {

    private final GoalAggregateService goalAggregateService;

    @PostMapping("/goals/{goalId}/sub-goals")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubGoalSaveResponse saveSubGoal(@PathVariable @Positive Long goalId,
                                           @RequestBody @Valid SubGoalSaveRequest request) {
        SubGoalSaveCommand command = new SubGoalSaveCommand(
                1L, goalId, request.getSubGoalName()
        );
        Long subGoalId = goalAggregateService.saveSubGoal(command);
        return new SubGoalSaveResponse(subGoalId, command.getName());
    }

    @PatchMapping("/goals/{goalId}/sub-goals/{subGoalId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public SubGoalNameModifyResponse changeSubGoalName(@PathVariable @Positive Long goalId,
                                                       @RequestBody @Valid SubGoalNameModifyRequest request) {
        SubGoalModifyNameCommand command = new SubGoalModifyNameCommand(
                1L, goalId, request.getSubGoalName(), request.getNewSubGoalName()
        );
        String changedSubGoalName = goalAggregateService.changeSubGoalName(command);
        return new SubGoalNameModifyResponse(goalId, changedSubGoalName);
    }

    @DeleteMapping("/goals/{goalId}/sub-goals")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubGoal(@PathVariable @Positive Long goalId,
                              @RequestParam @NotBlank String subGoalName) {
        SubGoalDeleteCommand command = new SubGoalDeleteCommand(1L, goalId, subGoalName);
        goalAggregateService.deleteSubGoal(command);
    }

    @PatchMapping("/goals/{goalId}/sub-goals/{subGoalId}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public SubGoalStatusModifyResponse changeSubGoalStatus(@PathVariable @Positive Long goalId,
                                                           @PathVariable @Positive Long subGoalId,
                                                           @RequestBody @Valid SubGoalStatusModifyRequest request) {
        SubGoalModifyStatusCommand command = new SubGoalModifyStatusCommand(
                1L, goalId, request.getSubGoalName(), request.getSubGoalStatus()
        );
        GoalStatus goalStatus = goalAggregateService.changeSubGoalStatus(command);

        return new SubGoalStatusModifyResponse(command.getName(), goalStatus, command.getSubGoalStatus());
    }

    @PatchMapping("/goals/{goalId}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public GoalStatusModifyResponse changeGoalStatus(@PathVariable @Positive Long goalId,
                                                     @RequestBody @Valid GoalStatusModifyRequest request) {
        GoalModifyStatusCommand command = new GoalModifyStatusCommand(
                1L, goalId, request.getGoalStatus()
        );
        GoalStatus changedGoalStatus = goalAggregateService.changeGoalStatus(command);
        return new GoalStatusModifyResponse(goalId, changedGoalStatus);
    }

    @DeleteMapping("/goals/{goalId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteGoal(@PathVariable @Positive Long goalId) {
        GoalDeleteCommand command = new GoalDeleteCommand(1L, goalId);
        goalAggregateService.deleteGoal(command);
    }

}
