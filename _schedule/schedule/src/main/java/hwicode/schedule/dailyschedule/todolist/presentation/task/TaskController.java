package hwicode.schedule.dailyschedule.todolist.presentation.task;

import hwicode.schedule.common.config.auth.LoginInfo;
import hwicode.schedule.common.config.auth.LoginUser;
import hwicode.schedule.dailyschedule.todolist.application.TaskAggregateService;
import hwicode.schedule.dailyschedule.todolist.application.dto.TaskInformationCommand;
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
    public TaskInformationModifyResponse changeTaskInformation(@LoginUser LoginInfo loginInfo,
                                                               @PathVariable @Positive Long taskId,
                                                               @RequestBody @Valid TaskInformationModifyRequest request) {
        TaskInformationCommand command = new TaskInformationCommand(loginInfo.getUserId(), taskId, request.getPriority(), request.getImportance());
        taskAggregateService.changeTaskInformation(command);
        return new TaskInformationModifyResponse(
                taskId,
                command.getPriority(),
                command.getImportance()
        );
    }
}
