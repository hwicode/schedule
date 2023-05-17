package hwicode.schedule.dailyschedule.todolist.presentation.task;

import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.application.TaskAggregateService;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyResponse;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class TaskController {

    private final TaskSaveAndDeleteService taskSaveAndDeleteService;
    private final TaskAggregateService taskAggregateService;

    @PostMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskSaveResponse saveTask(@PathVariable @Positive Long dailyToDoListId,
                                     @RequestBody @Valid TaskSaveRequest taskSaveRequest) {
        Long taskId = taskSaveAndDeleteService.save(taskSaveRequest);
        return new TaskSaveResponse(taskId, taskSaveRequest.getTaskName());
    }

    @DeleteMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable @Positive Long dailyToDoListId,
                           @PathVariable @Positive Long taskId,
                           @RequestBody @Valid TaskDeleteRequest taskDeleteRequest) {
        taskSaveAndDeleteService.delete(taskDeleteRequest.getTaskName(), taskDeleteRequest);
    }

    @PatchMapping("/dailyschedule/tasks/{taskId}/information")
    @ResponseStatus(value = HttpStatus.OK)
    public TaskInformationModifyResponse changeTaskInformation(@PathVariable @Positive Long taskId,
                                                               @RequestBody @Valid TaskInformationModifyRequest taskInformationModifyRequest) {
        taskAggregateService.changeTaskInformation(taskId, taskInformationModifyRequest);
        return new TaskInformationModifyResponse(
                taskId,
                taskInformationModifyRequest.getPriority(),
                taskInformationModifyRequest.getImportance()
        );
    }
}
