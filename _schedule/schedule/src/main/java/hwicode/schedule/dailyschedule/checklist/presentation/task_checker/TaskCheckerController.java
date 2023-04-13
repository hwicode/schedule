package hwicode.schedule.dailyschedule.checklist.presentation.task_checker;

import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyResponse;
import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class TaskCheckerController {

    private final TaskCheckerService taskCheckerService;

    @PatchMapping("/dailyschedule/checklist/taskCheckers/{taskCheckerName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskStatusModifyResponse changeTaskStatus(@PathVariable @NotBlank String taskCheckerName,
                                                     @RequestBody @Valid TaskStatusModifyRequest taskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = taskCheckerService.changeTaskStatus(taskCheckerName, taskStatusModifyRequest);
        return new TaskStatusModifyResponse(taskCheckerName, modifiedTaskStatus);
    }

    @PatchMapping("/dailyschedule/checklist/taskCheckers/{taskCheckerName}/difficulty")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDifficultyModifyResponse changeTaskDifficulty(@PathVariable @NotBlank String taskCheckerName,
                                                             @RequestBody @Valid TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        Difficulty modifiedDifficulty = taskCheckerService.changeTaskDifficulty(taskCheckerName, taskDifficultyModifyRequest);
        return new TaskDifficultyModifyResponse(taskCheckerName, modifiedDifficulty);
    }
}
