package hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save;

import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskCheckerSaveRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskName;

    public TaskCheckerSaveRequest(Long dailyChecklistId, String taskName) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
    }

    public TaskChecker toEntity() {
        return new TaskChecker(taskName);
    }
}
