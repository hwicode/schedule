package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskStatusModifyResponse {

    private String taskName;
    private Status modifiedStatus;

    public TaskStatusModifyResponse(String taskName, Status modifiedStatus) {
        this.taskName = taskName;
        this.modifiedStatus = modifiedStatus;
    }
}
