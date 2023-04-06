package hwicode.schedule.dailyschedule.checklist.presentation.subtask;

import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.save.SubTaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify.SubTaskStatusModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class SubTaskController {

    private final SubTaskCheckerService subTaskCheckerService;

    @PostMapping("/dailyschedule/checklist/subtasks")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubTaskSaveResponse saveSubTask(@RequestBody @Valid SubTaskSaveRequest subTaskSaveRequest) {
        Long subTaskId = subTaskCheckerService.saveSubTask(subTaskSaveRequest);
        return new SubTaskSaveResponse(subTaskId, subTaskSaveRequest.getSubTaskName());
    }

    @DeleteMapping("/dailyschedule/checklist/subtasks/{subTaskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubTask(@PathVariable @NotBlank String subTaskName,
                              @RequestBody @Valid SubTaskDeleteRequest subTaskDeleteRequest) {
        subTaskCheckerService.deleteSubTask(subTaskName, subTaskDeleteRequest);
    }

    @PatchMapping("/dailyschedule/checklist/subtasks/{subTaskName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskStatusModifyResponse changeTaskStatus(@PathVariable @NotBlank String subTaskName,
                                                        @RequestBody @Valid SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = subTaskCheckerService.changeSubTaskStatus(subTaskName, subTaskStatusModifyRequest);
        return new SubTaskStatusModifyResponse(subTaskName, modifiedTaskStatus, subTaskStatusModifyRequest.getSubTaskStatus());
    }
}
