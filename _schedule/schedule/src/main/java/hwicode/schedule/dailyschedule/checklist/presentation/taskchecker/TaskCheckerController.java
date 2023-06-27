package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker;

import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyResponse;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class TaskCheckerController {

    private final TaskCheckerSubService taskCheckerSubService;

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskStatusModifyResponse changeTaskStatus(@PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                     @PathVariable("taskId") @Positive Long taskCheckerId,
                                                     @RequestBody @Valid TaskStatusModifyRequest taskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = taskCheckerSubService.changeTaskStatus(
                taskStatusModifyRequest.getTaskCheckerName(), taskStatusModifyRequest
        );
        return new TaskStatusModifyResponse(taskStatusModifyRequest.getTaskCheckerName(), modifiedTaskStatus);
    }

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/difficulty")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDifficultyModifyResponse changeTaskDifficulty(@PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                             @PathVariable("taskId") @Positive Long taskCheckerId,
                                                             @RequestBody @Valid TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        Difficulty modifiedDifficulty = taskCheckerSubService.changeTaskDifficulty(
                taskDifficultyModifyRequest.getTaskCheckerName(), taskDifficultyModifyRequest
        );
        return new TaskDifficultyModifyResponse(taskDifficultyModifyRequest.getTaskCheckerName(), modifiedDifficulty);
    }

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskCheckerNameModifyResponse changeTaskCheckerName(@PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                               @PathVariable("taskId") @Positive Long taskCheckerId,
                                                               @RequestBody @Valid TaskCheckerNameModifyRequest taskCheckerNameModifyRequest) {
        String newTaskCheckerName = taskCheckerSubService.changeTaskCheckerName(
                taskCheckerNameModifyRequest.getTaskCheckerName(), taskCheckerNameModifyRequest
        );
        return new TaskCheckerNameModifyResponse(taskCheckerNameModifyRequest.getDailyChecklistId(), newTaskCheckerName);
    }
}
