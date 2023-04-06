package hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskCheckerSaveResponse {

    @NotNull @Positive
    private Long taskId;

    @NotBlank
    private String taskName;

    public TaskCheckerSaveResponse(Long taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }
}
