package hwicode.schedule.dailyschedule.todolist.presentation.task;

import hwicode.schedule.dailyschedule.todolist.application.TaskAggregateService;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyResponse;
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

    private final TaskAggregateService taskAggregateService;

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
