package hwicode.schedule.dailyschedule.checklist.presentation;

import hwicode.schedule.dailyschedule.checklist.application.SubTaskService;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify.SubTaskStatusModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
class SubTaskController {

    private final SubTaskService subTaskService;

    @PostMapping("/subtasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubTaskSaveResponse saveSubTask(@RequestBody @Valid SubTaskSaveRequest subTaskSaveRequest) {
        Long subTaskId = subTaskService.saveSubTask(subTaskSaveRequest);
        return new SubTaskSaveResponse(subTaskId, subTaskSaveRequest.getSubTaskName());
    }

    @DeleteMapping("/subtasks/{subTaskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubTask(@PathVariable @NotBlank String subTaskName,
                              @RequestBody @Valid SubTaskDeleteRequest subTaskDeleteRequest) {
        subTaskService.deleteSubTask(subTaskName, subTaskDeleteRequest);
    }

    @PatchMapping("/subtasks/{subTaskName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskStatusModifyResponse changeTaskStatus(@PathVariable @NotBlank String subTaskName,
                                                        @RequestBody @Valid SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        Status modifiedStatus = subTaskService.changeSubTaskStatus(subTaskName, subTaskStatusModifyRequest);
        return new SubTaskStatusModifyResponse(subTaskName, modifiedStatus);
    }
}
