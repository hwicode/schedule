package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker;

import hwicode.schedule.common.login.LoginInfo;
import hwicode.schedule.common.login.LoginUser;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerAggregateService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.SubTaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskDeleteCommand;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskNameModifyCommand;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskSaveCommand;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskStatusModifyCommand;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.save.SubTaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyResponse;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
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
public class SubTaskCheckerController {

    private final SubTaskCheckerSubService subTaskCheckerSubService;
    private final TaskCheckerAggregateService taskCheckerAggregateService;

    @PostMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubTaskSaveResponse saveSubTask(@LoginUser LoginInfo loginInfo,
                                           @PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                           @PathVariable("taskId")  @Positive Long taskCheckerId,
                                           @RequestBody @Valid SubTaskSaveRequest request) {
        SubTaskSaveCommand command = new SubTaskSaveCommand(
                loginInfo.getUserId(), dailyChecklistId, request.getTaskName(), request.getSubTaskName()
        );
        Long subTaskId = subTaskCheckerSubService.saveSubTaskChecker(command);
        return new SubTaskSaveResponse(subTaskId, command.getSubTaskCheckerName());
    }

    @DeleteMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubTask(@LoginUser LoginInfo loginInfo,
                              @PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                              @PathVariable("taskId") @Positive Long taskCheckerId,
                              @PathVariable("subTaskId") @Positive Long subTaskCheckerId,
                              @RequestParam("taskName") @NotBlank String taskCheckerName,
                              @RequestParam("subTaskName") @NotBlank String subTaskCheckerName) {
        SubTaskDeleteCommand command = new SubTaskDeleteCommand(
                loginInfo.getUserId(), dailyChecklistId, taskCheckerName, subTaskCheckerName, subTaskCheckerId
        );
        subTaskCheckerSubService.deleteSubTaskChecker(command);
    }

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskStatusModifyResponse changeSubTaskStatus(@LoginUser LoginInfo loginInfo,
                                                           @PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                           @PathVariable("taskId") @Positive Long taskCheckerId,
                                                           @PathVariable("subTaskId") @Positive Long subTaskCheckerId,
                                                           @RequestBody @Valid SubTaskStatusModifyRequest request) {
        SubTaskStatusModifyCommand command = new SubTaskStatusModifyCommand(
                loginInfo.getUserId(), dailyChecklistId, request.getTaskName(), request.getSubTaskName(), request.getSubTaskStatus()
        );
        TaskStatus modifiedTaskStatus = subTaskCheckerSubService.changeSubTaskStatus(command);
        return new SubTaskStatusModifyResponse(command.getSubTaskCheckerName(), modifiedTaskStatus, command.getSubTaskStatus());
    }

    @PatchMapping("/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskCheckerNameModifyResponse changeSubTaskCheckerName(@LoginUser LoginInfo loginInfo,
                                                                     @PathVariable("taskId") @Positive Long taskCheckerId,
                                                                     @PathVariable("subTaskId") @Positive Long subTaskCheckerId,
                                                                     @RequestBody @Valid SubTaskCheckerNameModifyRequest request) {
        SubTaskNameModifyCommand command = new SubTaskNameModifyCommand(
                loginInfo.getUserId(), taskCheckerId, request.getSubTaskName(), request.getNewSubTaskName()
        );
        String newSubTaskCheckerName = taskCheckerAggregateService.changeSubTaskCheckerName(command);
        return new SubTaskCheckerNameModifyResponse(command.getTaskCheckerId(), newSubTaskCheckerName);
    }
}
