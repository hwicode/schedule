package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class SubTaskStatusModifyRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskName;

    @NotNull
    private Status status;

    public SubTaskStatusModifyRequest(Long dailyChecklistId, String taskName, Status status) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
        this.status = status;
    }
}
