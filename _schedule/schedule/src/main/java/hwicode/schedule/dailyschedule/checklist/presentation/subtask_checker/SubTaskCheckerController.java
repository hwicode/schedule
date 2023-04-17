package hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker;

import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.name_modify.SubTaskCheckerNameModifyResponse;
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
    public SubTaskCheckerSaveResponse saveSubTaskChecker(@RequestBody @Valid SubTaskCheckerSaveRequest subTaskCheckerSaveRequest) {
        Long subTaskCheckerId = subTaskCheckerService.saveSubTaskChecker(subTaskCheckerSaveRequest);
        return new SubTaskCheckerSaveResponse(subTaskCheckerId, subTaskCheckerSaveRequest.getSubTaskCheckerName());
    }

    @DeleteMapping("/dailyschedule/checklist/subtaskCheckers/{subTaskCheckerName}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubTaskChecker(@PathVariable @NotBlank String subTaskCheckerName,
                                     @RequestBody @Valid SubTaskCheckerDeleteRequest subTaskCheckerDeleteRequest) {
        subTaskCheckerService.deleteSubTaskChecker(subTaskCheckerName, subTaskCheckerDeleteRequest);
    }

    @PatchMapping("/dailyschedule/checklist/subtaskCheckers/{subTaskCheckerName}/status")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskStatusModifyResponse changeTaskStatus(@PathVariable @NotBlank String subTaskCheckerName,
                                                        @RequestBody @Valid SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        TaskStatus modifiedTaskStatus = subTaskCheckerService.changeSubTaskStatus(subTaskCheckerName, subTaskStatusModifyRequest);
        return new SubTaskStatusModifyResponse(subTaskCheckerName, modifiedTaskStatus, subTaskStatusModifyRequest.getSubTaskStatus());
    }

    @PatchMapping("/dailyschedule/checklist/subtaskCheckers/{subTaskCheckerName}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public SubTaskCheckerNameModifyResponse changeSubTaskCheckerName(@PathVariable @NotBlank String subTaskCheckerName,
                               @RequestBody @Valid SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest) {
        String newSubTaskCheckerName = subTaskCheckerService.changeSubTaskCheckerName(subTaskCheckerName, subTaskCheckerNameModifyRequest);
        return new SubTaskCheckerNameModifyResponse(subTaskCheckerNameModifyRequest.getTaskCheckerId(), newSubTaskCheckerName);
    }
}
