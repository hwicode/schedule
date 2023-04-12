package hwicode.schedule.dailyschedule.checklist.presentation.task_checker;

import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.delete.TaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class TaskCheckerController {

    private final TaskCheckerService taskCheckerService;

    @PostMapping("/dailyschedule/checklist/taskCheckers")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskCheckerSaveResponse saveTask(@RequestBody @Valid TaskCheckerSaveRequest taskCheckerSaveRequest) {
        Long taskId = taskCheckerService.saveTask(taskCheckerSaveRequest);
        return new TaskCheckerSaveResponse(taskId, taskCheckerSaveRequest.getTaskCheckerName());
    }

    @DeleteMapping("/dailyschedule/checklist/taskCheckers/{taskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable @NotBlank String taskName,
                           @RequestBody @Valid TaskCheckerDeleteRequest taskCheckerDeleteRequest) {
        taskCheckerService.deleteTask(taskCheckerDeleteRequest.getDailyChecklistId(), taskName);
    }

    @PatchMapping("/dailyschedule/checklist/taskCheckers/{taskName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskStatusModifyResponse changeTaskStatus(@PathVariable @NotBlank String taskName,
                                                     @RequestBody @Valid TaskStatusModifyRequest taskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = taskCheckerService.changeTaskStatus(taskName, taskStatusModifyRequest);
        return new TaskStatusModifyResponse(taskName, modifiedTaskStatus);
    }

    @PatchMapping("/dailyschedule/checklist/taskCheckers/{taskName}/difficulty")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDifficultyModifyResponse changeTaskDifficulty(@PathVariable @NotBlank String taskName,
                                                             @RequestBody @Valid TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        Difficulty modifiedDifficulty = taskCheckerService.changeTaskDifficulty(taskName, taskDifficultyModifyRequest);
        return new TaskDifficultyModifyResponse(taskName, modifiedDifficulty);
    }

}
