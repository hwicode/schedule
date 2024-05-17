package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker;

import hwicode.schedule.common.login.LoginInfo;
import hwicode.schedule.common.login.LoginUser;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.task_checker.*;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save.TaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyResponse;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
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
public class TaskCheckerController {

    private final TaskCheckerSubService taskCheckerSubService;

    @PostMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskSaveResponse saveTask(@LoginUser LoginInfo loginInfo,
                                     @PathVariable @Positive Long dailyToDoListId,
                                     @RequestBody @Valid TaskSaveRequest request) {

        TaskSaveCommand command = new TaskSaveCommand(
                loginInfo.getUserId(), request.getDailyToDoListId(), request.getTaskName(),
                request.getDifficulty(), request.getPriority(), request.getImportance()
        );
        Long taskId = taskCheckerSubService.saveTaskChecker(command);
        return new TaskSaveResponse(taskId, command.getTaskCheckerName());
    }

    @DeleteMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@LoginUser LoginInfo loginInfo,
                           @PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                           @PathVariable @Positive Long taskId,
                           @RequestParam("taskName") @NotBlank String taskCheckerName) {
        TaskDeleteCommand command = new TaskDeleteCommand(loginInfo.getUserId(), dailyChecklistId, taskId, taskCheckerName);
        taskCheckerSubService.deleteTaskChecker(command);
    }

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskStatusModifyResponse changeTaskStatus(@LoginUser LoginInfo loginInfo,
                                                     @PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                     @PathVariable("taskId") @Positive Long taskCheckerId,
                                                     @RequestBody @Valid TaskStatusModifyRequest request) {
        TaskStatusModifyCommand command = new TaskStatusModifyCommand(
                loginInfo.getUserId(), dailyChecklistId, request.getTaskName(), request.getTaskStatus()
        );
        TaskStatus modifiedTaskStatus = taskCheckerSubService.changeTaskStatus(command);
        return new TaskStatusModifyResponse(command.getTaskCheckerName(), modifiedTaskStatus);
    }

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/difficulty")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDifficultyModifyResponse changeTaskDifficulty(@LoginUser LoginInfo loginInfo,
                                                             @PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                             @PathVariable("taskId") @Positive Long taskCheckerId,
                                                             @RequestBody @Valid TaskDifficultyModifyRequest request) {
        TaskDifficultyModifyCommand command = new TaskDifficultyModifyCommand(
                loginInfo.getUserId(), dailyChecklistId, request.getTaskName(), request.getDifficulty()
        );
        Difficulty modifiedDifficulty = taskCheckerSubService.changeTaskDifficulty(command);
        return new TaskDifficultyModifyResponse(command.getTaskCheckerName(), modifiedDifficulty);
    }

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskCheckerNameModifyResponse changeTaskCheckerName(@LoginUser LoginInfo loginInfo,
                                                               @PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                               @PathVariable("taskId") @Positive Long taskCheckerId,
                                                               @RequestBody @Valid TaskCheckerNameModifyRequest request) {
        TaskCheckerNameModifyCommand command = new TaskCheckerNameModifyCommand(
                loginInfo.getUserId(), dailyChecklistId, request.getTaskName(), request.getNewTaskName()
        );
        String newTaskCheckerName = taskCheckerSubService.changeTaskCheckerName(command);
        return new TaskCheckerNameModifyResponse(command.getDailyChecklistId(), newTaskCheckerName);
    }
}
