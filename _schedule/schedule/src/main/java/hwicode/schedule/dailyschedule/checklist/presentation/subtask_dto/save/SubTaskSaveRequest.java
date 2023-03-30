package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save;

import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubTaskSaveRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskName;

    @NotBlank
    private String subTaskName;

    public SubTaskSaveRequest(Long dailyChecklistId, String taskName, String subTaskName) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
        this.subTaskName = subTaskName;
    }

    public SubTask toEntity() {
        return new SubTask(subTaskName);
    }
}
