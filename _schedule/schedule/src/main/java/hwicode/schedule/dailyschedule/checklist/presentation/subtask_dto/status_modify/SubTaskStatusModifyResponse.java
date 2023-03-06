package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;

public class SubTaskStatusModifyResponse {

    private String subTaskName;
    private Status modifiedStatus;

    public SubTaskStatusModifyResponse() {}

    public SubTaskStatusModifyResponse(String subTaskName, Status modifiedStatus) {
        this.subTaskName = subTaskName;
        this.modifiedStatus = modifiedStatus;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public Status getModifiedStatus() {
        return modifiedStatus;
    }
}
