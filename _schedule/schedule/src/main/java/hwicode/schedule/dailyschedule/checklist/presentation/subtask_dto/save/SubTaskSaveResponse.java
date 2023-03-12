package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class SubTaskSaveResponse {

    @NotNull @Positive
    private Long subTaskId;

    @NotBlank
    private String subTaskName;

    public SubTaskSaveResponse(Long subTaskId, String subTaskName) {
        this.subTaskId = subTaskId;
        this.subTaskName = subTaskName;
    }
}
