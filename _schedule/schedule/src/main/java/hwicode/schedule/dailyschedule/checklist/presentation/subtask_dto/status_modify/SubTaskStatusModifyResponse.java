package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubTaskStatusModifyResponse {

    private String subTaskName;
    private Status modifiedStatus;

    public SubTaskStatusModifyResponse(String subTaskName, Status modifiedStatus) {
        this.subTaskName = subTaskName;
        this.modifiedStatus = modifiedStatus;
    }
}
