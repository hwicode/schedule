package hwicode.schedule.dailyschedule.checklist.presentation;

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
