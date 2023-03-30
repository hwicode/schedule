package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskStatusModifyResponse {

    @NotBlank
    private String taskName;

    @NotNull
    private TaskStatus taskStatus;

    public TaskStatusModifyResponse(String taskName, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.taskStatus = taskStatus;
    }
}
