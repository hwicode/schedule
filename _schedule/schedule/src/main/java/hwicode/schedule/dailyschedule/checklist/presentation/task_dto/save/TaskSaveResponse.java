package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskSaveResponse {

    private Long taskId;
    private String taskName;

    public TaskSaveResponse(Long taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }
}
