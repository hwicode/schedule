package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;

public class TaskStatusModifyResponse {

    private String taskName;
    private Status modifiedStatus;

    public TaskStatusModifyResponse() {}

    public TaskStatusModifyResponse(String taskName, Status modifiedStatus) {
        this.taskName = taskName;
        this.modifiedStatus = modifiedStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public Status getModifiedStatus() {
        return modifiedStatus;
    }
}
