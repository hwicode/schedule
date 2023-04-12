package hwicode.schedule.dailyschedule.todolist.presentation.task;

import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskSaveAndDeleteService taskSaveAndDeleteService;

    @PostMapping("/dailyschedule/todolist/tasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskSaveResponse saveTask(@RequestBody @Valid TaskSaveRequest taskSaveRequest) {
        Long taskId = taskSaveAndDeleteService.save(taskSaveRequest);
        return new TaskSaveResponse(taskId, taskSaveRequest.getTaskName());
    }

    @DeleteMapping("/dailyschedule/todolist/tasks/{taskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable @NotBlank String taskName,
                                  @RequestBody @Valid TaskDeleteRequest taskDeleteRequest) {
        taskSaveAndDeleteService.delete(taskName, taskDeleteRequest);
    }
}
