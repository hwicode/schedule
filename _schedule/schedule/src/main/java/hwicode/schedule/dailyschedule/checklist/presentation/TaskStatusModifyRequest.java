package hwicode.schedule.dailyschedule.checklist.presentation;

import hwicode.schedule.dailyschedule.checklist.domain.Status;

public class TaskStatusModifyRequest {

    private Long dailyChecklistId;
    private Status status;

    public TaskStatusModifyRequest() {
    }

    public TaskStatusModifyRequest(Long dailyChecklistId, Status status) {
        this.dailyChecklistId = dailyChecklistId;
        this.status = status;
    }

    public Long getDailyChecklistId() {
        return dailyChecklistId;
    }

    public Status getStatus() {
        return status;
    }
}
