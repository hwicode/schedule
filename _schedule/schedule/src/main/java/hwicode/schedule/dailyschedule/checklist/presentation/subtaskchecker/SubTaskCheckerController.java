package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker;

import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerAggregateService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.SubTaskCheckerSubService;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class SubTaskCheckerController {

    private final SubTaskCheckerSubService subTaskCheckerSubService;
    private final TaskCheckerAggregateService taskCheckerAggregateService;

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskStatusModifyResponse changeSubTaskStatus(@PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                           @PathVariable("taskId") @Positive Long taskCheckerId,
                                                           @PathVariable("subTaskId") @Positive Long subTaskCheckerId,
                                                           @RequestBody @Valid SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = subTaskCheckerSubService.changeSubTaskStatus(
                subTaskStatusModifyRequest.getSubTaskCheckerName(), subTaskStatusModifyRequest
        );
        return new SubTaskStatusModifyResponse(subTaskStatusModifyRequest.getSubTaskCheckerName(), modifiedTaskStatus, subTaskStatusModifyRequest.getSubTaskStatus());
    }

    @PatchMapping("/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskCheckerNameModifyResponse changeSubTaskCheckerName(@PathVariable("taskId") @Positive Long taskCheckerId,
                                                                     @PathVariable("subTaskId") @Positive Long subTaskCheckerId,
                                                                     @RequestBody @Valid SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest) {
        String newSubTaskCheckerName = taskCheckerAggregateService.changeSubTaskCheckerName(
                subTaskCheckerNameModifyRequest.getSubTaskCheckerName(), subTaskCheckerNameModifyRequest
        );
        return new SubTaskCheckerNameModifyResponse(subTaskCheckerNameModifyRequest.getTaskCheckerId(), newSubTaskCheckerName);
    }
}
