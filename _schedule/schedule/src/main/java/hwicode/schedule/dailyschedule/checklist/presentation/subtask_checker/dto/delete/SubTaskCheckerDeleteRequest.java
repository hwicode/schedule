package hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.delete;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubTaskCheckerDeleteRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskName;

    public SubTaskCheckerDeleteRequest(Long dailyChecklistId, String taskName) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
    }
}
