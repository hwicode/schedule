package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubTaskStatusModifyRequest {

    private Long dailyChecklistId;
    private String taskName;
    private Status status;

    public SubTaskStatusModifyRequest(Long dailyChecklistId, String taskName, Status status) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
        this.status = status;
    }
}
