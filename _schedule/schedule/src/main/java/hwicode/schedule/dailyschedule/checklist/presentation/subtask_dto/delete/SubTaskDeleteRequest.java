package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete;

public class SubTaskDeleteRequest {

    private Long dailyChecklistId;
    private String taskName;

    public SubTaskDeleteRequest() {
    }

    public SubTaskDeleteRequest(Long dailyChecklistId, String taskName) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
    }

    public Long getDailyChecklistId() {
        return dailyChecklistId;
    }
    public String getTaskName() {
        return taskName;
    }
}
