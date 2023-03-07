package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save;

import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubTaskSaveRequest {

    private Long dailyChecklistId;
    private String taskName;
    private String subTaskName;

    public SubTaskSaveRequest(Long dailyChecklistId, String taskName, String subTaskName) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
        this.subTaskName = subTaskName;
    }

    public SubTask toEntity() {
        return new SubTask(subTaskName);
    }
}
