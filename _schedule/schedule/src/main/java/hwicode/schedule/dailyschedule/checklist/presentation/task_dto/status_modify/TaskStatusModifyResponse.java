package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class TaskStatusModifyResponse {

    @NotBlank
    private String taskName;

    @NotNull
    private Status modifiedStatus;

    public TaskStatusModifyResponse(String taskName, Status modifiedStatus) {
        this.taskName = taskName;
        this.modifiedStatus = modifiedStatus;
    }
}
