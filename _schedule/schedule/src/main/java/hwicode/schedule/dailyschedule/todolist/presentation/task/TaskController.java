package hwicode.schedule.dailyschedule.todolist.presentation.task;

import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.application.TaskService;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyResponse;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskSaveAndDeleteService taskSaveAndDeleteService;
    private final TaskService taskService;

    @PostMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskSaveResponse saveTask(@PathVariable Long dailyToDoListId,
                                     @RequestBody @Valid TaskSaveRequest taskSaveRequest) {
        Long taskId = taskSaveAndDeleteService.save(taskSaveRequest);
        return new TaskSaveResponse(taskId, taskSaveRequest.getTaskName());
    }

    @DeleteMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long dailyToDoListId,
                           @PathVariable @NotBlank String taskName,
                           @RequestBody @Valid TaskDeleteRequest taskDeleteRequest) {
        taskSaveAndDeleteService.delete(taskName, taskDeleteRequest);
    }

    @PatchMapping("/dailyschedule/tasks/{taskId}/information")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskInformationModifyResponse changeTaskInformation(@PathVariable @NotNull Long taskId,
                                                               @RequestBody @Valid TaskInformationModifyRequest taskInformationModifyRequest) {
        taskService.changeTaskInformation(taskId, taskInformationModifyRequest);
        return new TaskInformationModifyResponse(
                taskId,
                taskInformationModifyRequest.getPriority(),
                taskInformationModifyRequest.getImportance()
        );
    }
}
