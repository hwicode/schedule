package hwicode.schedule.dailyschedule.checklist.presentation;

import hwicode.schedule.dailyschedule.checklist.domain.Task;

public class TaskSaveRequest {

    private Long dailyChecklistId;
    private String taskName;

    public TaskSaveRequest() {
    }

    public TaskSaveRequest(Long dailyChecklistId, String taskName) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
    }

    public Task toEntity() {
        return new Task(taskName);
    }

    public Long getDailyChecklistId() {
        return dailyChecklistId;
    }

    public String getTaskName() {
        return taskName;
    }
}
