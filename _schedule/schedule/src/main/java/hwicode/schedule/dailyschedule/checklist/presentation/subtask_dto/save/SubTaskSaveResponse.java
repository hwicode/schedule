package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save;

public class SubTaskSaveResponse {

    private Long subTaskId;
    private String subTaskName;

    public SubTaskSaveResponse() {}

    public SubTaskSaveResponse(Long subTaskId, String subTaskName) {
        this.subTaskId = subTaskId;
        this.subTaskName = subTaskName;
    }

    public Long getSubTaskId() {
        return subTaskId;
    }

    public String getSubTaskName() {
        return subTaskName;
    }
}
