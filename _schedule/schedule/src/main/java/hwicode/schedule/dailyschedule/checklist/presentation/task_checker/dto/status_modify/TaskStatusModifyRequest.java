package hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify;

import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskStatusModifyRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotNull
    private TaskStatus taskStatus;

    public TaskStatusModifyRequest(Long dailyChecklistId, TaskStatus taskStatus) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskStatus = taskStatus;
    }
}
