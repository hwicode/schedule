package hwicode.schedule.dailyschedule.todolist.presentation;

import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
