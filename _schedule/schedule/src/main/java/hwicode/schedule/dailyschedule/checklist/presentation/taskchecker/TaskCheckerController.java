package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker;

import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
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

    private final TaskCheckerService taskCheckerService;

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskStatusModifyResponse changeTaskStatus(@PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                     @PathVariable("taskName") String taskCheckerName,
                                                     @RequestBody @Valid TaskStatusModifyRequest taskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = taskCheckerService.changeTaskStatus(taskCheckerName, taskStatusModifyRequest);
        return new TaskStatusModifyResponse(taskCheckerName, modifiedTaskStatus);
    }

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskName}/difficulty")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDifficultyModifyResponse changeTaskDifficulty(@PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                             @PathVariable("taskName") String taskCheckerName,
                                                             @RequestBody @Valid TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        Difficulty modifiedDifficulty = taskCheckerService.changeTaskDifficulty(taskCheckerName, taskDifficultyModifyRequest);
        return new TaskDifficultyModifyResponse(taskCheckerName, modifiedDifficulty);
    }

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskName}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskCheckerNameModifyResponse changeTaskCheckerName(@PathVariable("dailyToDoListId") @Positive Long dailyChecklistId,
                                                               @PathVariable("taskName") String taskCheckerName,
                                                               @RequestBody @Valid TaskCheckerNameModifyRequest taskCheckerNameModifyRequest) {
        String newTaskCheckerName = taskCheckerService.changeTaskCheckerName(taskCheckerName, taskCheckerNameModifyRequest);
        return new TaskCheckerNameModifyResponse(taskCheckerNameModifyRequest.getDailyChecklistId(), newTaskCheckerName);
    }
}
