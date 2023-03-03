package hwicode.schedule.dailyschedule.checklist.presentation;

import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskSaveResponse saveTask(@RequestBody TaskSaveRequest taskSaveRequest) {
        Long taskId = taskService.saveTask(taskSaveRequest);
        return new TaskSaveResponse(taskId, taskSaveRequest.getTaskName());
    }

    @DeleteMapping("/tasks/{taskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String taskName, @RequestBody TaskDeleteRequest taskDeleteRequest) {
        taskService.deleteTask(taskDeleteRequest.getDailyChecklistId(), taskName);
    }

    @PatchMapping("/tasks/{taskName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskStatusModifyResponse changeTaskStatus(@PathVariable String taskName, @RequestBody TaskStatusModifyRequest taskStatusModifyRequest) {
        Status modifiedStatus = taskService.changeTaskStatus(taskName, taskStatusModifyRequest);
        return new TaskStatusModifyResponse(taskName, modifiedStatus);
    }

    @PatchMapping("/tasks/{taskName}/difficulty")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskDifficultyModifyResponse changeTaskDifficulty(@PathVariable String taskName, @RequestBody TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        Difficulty modifiedDifficulty = taskService.changeTaskDifficulty(taskName, taskDifficultyModifyRequest);
        return new TaskDifficultyModifyResponse(taskName, modifiedDifficulty);
    }

}
