package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class SubTaskDeleteRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskName;

    public SubTaskDeleteRequest(Long dailyChecklistId, String taskName) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
    }
}
