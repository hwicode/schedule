package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class SubTaskStatusModifyResponse {

    @NotBlank
    private String subTaskName;

    @NotNull
    private Status taskStatus;

    @NotNull
    private Status subTaskStatus;

    public SubTaskStatusModifyResponse(String subTaskName, Status taskStatus, Status subTaskStatus) {
        this.subTaskName = subTaskName;
        this.taskStatus = taskStatus;
        this.subTaskStatus = subTaskStatus;
    }
}
