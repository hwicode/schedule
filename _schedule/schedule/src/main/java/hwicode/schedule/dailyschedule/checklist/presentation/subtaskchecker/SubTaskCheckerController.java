package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker;

import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class SubTaskCheckerController {

    private final SubTaskCheckerService subTaskCheckerService;

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskName}/subtasks/{subTaskName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskStatusModifyResponse changeSubTaskStatus(@PathVariable("dailyToDoListId") Long dailyChecklistId,
                                                           @PathVariable("taskName") String taskCheckerName,
                                                           @PathVariable("subTaskName") @NotBlank String subTaskCheckerName,
                                                           @RequestBody @Valid SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = subTaskCheckerService.changeSubTaskStatus(subTaskCheckerName, subTaskStatusModifyRequest);
        return new SubTaskStatusModifyResponse(subTaskCheckerName, modifiedTaskStatus, subTaskStatusModifyRequest.getSubTaskStatus());
    }

    @PatchMapping("/dailyschedule/tasks/{taskId}/subtasks/{subTaskName}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskCheckerNameModifyResponse changeSubTaskCheckerName(@PathVariable("taskId") Long taskCheckerId,
                                                                     @PathVariable("subTaskName") @NotBlank String subTaskCheckerName,
                                                                     @RequestBody @Valid SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest) {
        String newSubTaskCheckerName = subTaskCheckerService.changeSubTaskCheckerName(subTaskCheckerName, subTaskCheckerNameModifyRequest);
        return new SubTaskCheckerNameModifyResponse(subTaskCheckerNameModifyRequest.getTaskCheckerId(), newSubTaskCheckerName);
    }
}
