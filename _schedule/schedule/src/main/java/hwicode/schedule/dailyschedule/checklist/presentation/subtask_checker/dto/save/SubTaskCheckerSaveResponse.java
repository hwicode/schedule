package hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubTaskCheckerSaveResponse {

    @NotNull @Positive
    private Long subTaskId;

    @NotBlank
    private String subTaskName;

    public SubTaskCheckerSaveResponse(Long subTaskId, String subTaskName) {
        this.subTaskId = subTaskId;
        this.subTaskName = subTaskName;
    }
}
