package hwicode.schedule.dailyschedule.todolist.presentation.subtask;

import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class SubTaskController {

    private final SubTaskSaveAndDeleteService subTaskSaveAndDeleteService;

    @PostMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubTaskSaveResponse saveSubTask(@PathVariable @Positive Long dailyToDoListId,
                                           @PathVariable @Positive Long taskId,
                                           @RequestBody @Valid SubTaskSaveRequest subTaskSaveRequest) {
        Long subTaskId = subTaskSaveAndDeleteService.save(subTaskSaveRequest);
        return new SubTaskSaveResponse(subTaskId, subTaskSaveRequest.getSubTaskName());
    }

    @DeleteMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubTask(@PathVariable @Positive Long dailyToDoListId,
                              @PathVariable @Positive Long taskId,
                              @PathVariable @Positive Long subTaskId,
                              @RequestBody @Valid SubTaskDeleteRequest subTaskDeleteRequest) {
        subTaskSaveAndDeleteService.delete(subTaskDeleteRequest.getSubTaskName(), subTaskDeleteRequest);
    }
}
