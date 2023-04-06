package hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify;

import hwicode.schedule.dailyschedule.SubTaskStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubTaskStatusModifyRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskName;

    @NotNull
    private SubTaskStatus subTaskStatus;

    public SubTaskStatusModifyRequest(Long dailyChecklistId, String taskName, SubTaskStatus subTaskStatus) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
        this.subTaskStatus = subTaskStatus;
    }
}
