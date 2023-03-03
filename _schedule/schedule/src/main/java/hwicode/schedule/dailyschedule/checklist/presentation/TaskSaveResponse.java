package hwicode.schedule.dailyschedule.checklist.presentation;

public class TaskSaveResponse {

    private Long taskId;
    private String taskName;

    public TaskSaveResponse() {
    }

    public TaskSaveResponse(Long taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }
}
