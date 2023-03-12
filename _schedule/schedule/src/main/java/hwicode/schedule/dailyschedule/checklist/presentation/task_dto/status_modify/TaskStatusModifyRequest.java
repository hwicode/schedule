package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class TaskStatusModifyRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotNull
    private Status status;

    public TaskStatusModifyRequest(Long dailyChecklistId, Status status) {
        this.dailyChecklistId = dailyChecklistId;
        this.status = status;
    }
}
