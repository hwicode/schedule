package hwicode.schedule.dailyschedule.checklist.presentation.task;

import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.save.TaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.status_modify.TaskStatusModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/dailyschedule/checklist/tasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskSaveResponse saveTask(@RequestBody @Valid TaskSaveRequest taskSaveRequest) {
        Long taskId = taskService.saveTask(taskSaveRequest);
        return new TaskSaveResponse(taskId, taskSaveRequest.getTaskName());
    }

    @DeleteMapping("/dailyschedule/checklist/tasks/{taskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable @NotBlank String taskName,
                           @RequestBody @Valid TaskDeleteRequest taskDeleteRequest) {
        taskService.deleteTask(taskDeleteRequest.getDailyChecklistId(), taskName);
    }

    @PatchMapping("/dailyschedule/checklist/tasks/{taskName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskStatusModifyResponse changeTaskStatus(@PathVariable @NotBlank String taskName,
                                                     @RequestBody @Valid TaskStatusModifyRequest taskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = taskService.changeTaskStatus(taskName, taskStatusModifyRequest);
        return new TaskStatusModifyResponse(taskName, modifiedTaskStatus);
    }

    @PatchMapping("/dailyschedule/checklist/tasks/{taskName}/difficulty")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDifficultyModifyResponse changeTaskDifficulty(@PathVariable @NotBlank String taskName,
                                                             @RequestBody @Valid TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        Difficulty modifiedDifficulty = taskService.changeTaskDifficulty(taskName, taskDifficultyModifyRequest);
        return new TaskDifficultyModifyResponse(taskName, modifiedDifficulty);
    }

}
