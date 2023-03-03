package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.delete;

public class TaskDeleteRequest {

    private Long dailyChecklistId;

    public TaskDeleteRequest() {
    }

    public TaskDeleteRequest(Long dailyChecklistId) {
        this.dailyChecklistId = dailyChecklistId;
    }

    public Long getDailyChecklistId() {
        return dailyChecklistId;
    }

}
