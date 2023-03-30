package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
