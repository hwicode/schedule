package hwicode.schedule.dailyschedule.todolist.presentation.subtask;

import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.save.SubTaskSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class SubTaskController {

    private final SubTaskSaveAndDeleteService subTaskSaveAndDeleteService;

    @PostMapping("/dailyschedule/todolist/subtasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubTaskSaveResponse saveSubTask(@RequestBody @Valid SubTaskSaveRequest subTaskSaveRequest) {
        Long subTaskId = subTaskSaveAndDeleteService.save(subTaskSaveRequest);
        return new SubTaskSaveResponse(subTaskId, subTaskSaveRequest.getSubTaskName());
    }

    @DeleteMapping("/dailyschedule/todolist/subtasks/{subTaskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubTask(@PathVariable @NotBlank String subTaskName,
                              @RequestBody @Valid SubTaskDeleteRequest subTaskDeleteRequest) {
        subTaskSaveAndDeleteService.delete(subTaskName, subTaskDeleteRequest);
    }
}
