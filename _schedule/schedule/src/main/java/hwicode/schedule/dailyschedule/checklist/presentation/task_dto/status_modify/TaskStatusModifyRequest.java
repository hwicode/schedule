package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskStatusModifyRequest {

    private Long dailyChecklistId;
    private Status status;

    public TaskStatusModifyRequest(Long dailyChecklistId, Status status) {
        this.dailyChecklistId = dailyChecklistId;
        this.status = status;
    }
}
