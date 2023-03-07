package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save;

import hwicode.schedule.dailyschedule.checklist.domain.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskSaveRequest {

    private Long dailyChecklistId;
    private String taskName;

    public TaskSaveRequest(Long dailyChecklistId, String taskName) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
    }

    public Task toEntity() {
        return new Task(taskName);
    }
}
