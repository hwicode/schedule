package hwicode.schedule.dailyschedule.todolist.presentation.subtask;

import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class SubTaskController {

    private final SubTaskSaveAndDeleteService subTaskSaveAndDeleteService;

    @PostMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskName}/subtasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubTaskSaveResponse saveSubTask(@PathVariable Long dailyToDoListId,
                                           @PathVariable String taskName,
                                           @RequestBody @Valid SubTaskSaveRequest subTaskSaveRequest) {
        Long subTaskId = subTaskSaveAndDeleteService.save(subTaskSaveRequest);
        return new SubTaskSaveResponse(subTaskId, subTaskSaveRequest.getSubTaskName());
    }

    @DeleteMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskName}/subtasks/{subTaskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubTask(@PathVariable Long dailyToDoListId,
                              @PathVariable String taskName,
                              @PathVariable @NotBlank String subTaskName) {
        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(dailyToDoListId, taskName);
        subTaskSaveAndDeleteService.delete(subTaskName, subTaskDeleteRequest);
    }
}
