package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskStatusModifyResponse {

    @NotBlank
    private String taskName;

    @NotNull
    private Status taskStatus;

    public TaskStatusModifyResponse(String taskName, Status taskStatus) {
        this.taskName = taskName;
        this.taskStatus = taskStatus;
    }
}
