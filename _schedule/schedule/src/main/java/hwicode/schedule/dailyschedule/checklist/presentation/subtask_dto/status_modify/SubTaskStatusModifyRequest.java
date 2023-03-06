package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;

public class SubTaskStatusModifyRequest {

    private Long dailyChecklistId;
    private String taskName;
    private Status status;

    public SubTaskStatusModifyRequest() {
    }

    public SubTaskStatusModifyRequest(Long dailyChecklistId, String taskName, Status status) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
        this.status = status;
    }

    public Long getDailyChecklistId() {
        return dailyChecklistId;
    }

    public String getTaskName() {
        return taskName;
    }

    public Status getStatus() {
        return status;
    }
}
