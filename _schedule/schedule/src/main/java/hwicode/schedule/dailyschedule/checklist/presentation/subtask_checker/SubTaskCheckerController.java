package hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker;

import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify.SubTaskStatusModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class SubTaskCheckerController {

    private final SubTaskCheckerService subTaskCheckerService;

    @PostMapping("/dailyschedule/checklist/subtaskCheckers")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubTaskCheckerSaveResponse saveSubTask(@RequestBody @Valid SubTaskCheckerSaveRequest subTaskCheckerSaveRequest) {
        Long subTaskId = subTaskCheckerService.saveSubTask(subTaskCheckerSaveRequest);
        return new SubTaskCheckerSaveResponse(subTaskId, subTaskCheckerSaveRequest.getSubTaskCheckerName());
    }

    @DeleteMapping("/dailyschedule/checklist/subtaskCheckers/{subTaskName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubTask(@PathVariable @NotBlank String subTaskName,
                              @RequestBody @Valid SubTaskCheckerDeleteRequest subTaskCheckerDeleteRequest) {
        subTaskCheckerService.deleteSubTask(subTaskName, subTaskCheckerDeleteRequest);
    }

    @PatchMapping("/dailyschedule/checklist/subtaskCheckers/{subTaskName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskStatusModifyResponse changeTaskStatus(@PathVariable @NotBlank String subTaskName,
                                                        @RequestBody @Valid SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = subTaskCheckerService.changeSubTaskStatus(subTaskName, subTaskStatusModifyRequest);
        return new SubTaskStatusModifyResponse(subTaskName, modifiedTaskStatus, subTaskStatusModifyRequest.getSubTaskStatus());
    }
}
